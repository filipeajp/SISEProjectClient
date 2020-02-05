package com.insure.client;
//  Criar Servidor
// Mudar a dir to script
// Correr no Terminal Wsimport.bat -s ..\src -keep -p com.insure.client http://localhost:8090/docstorage?wsdl

import javax.swing.*;
import javax.xml.ws.BindingProvider;

public class Main {
	private static final String identifier = "";
	private static final String userType = "";

	/*
	 * The main function initialize the remote instance and connects to the remote service
	 * */
	public static void main (String[] args) throws java.lang.Exception {
		ClaimDataStoreService claimdatastoreService = new ClaimDataStoreService();

		ClaimDataStore claimDataStore = (ClaimDataStore) claimdatastoreService.getClaimDataStorePort();

		((BindingProvider) claimDataStore).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"http://localhost:8090/docstorage");

		runClaimDataBase(claimDataStore, userType, identifier);

	}

	/*
	 * This method executes the ClaimData client by receiving an expression as input
	 * from the user with numbers, multiplications and summations and printing the result
	 * ClaimDataStore will run until the user input 'exit'
	 * */
	public static void runClaimDataBase (ClaimDataStore claimDataStore, String userType, String identifier) throws java.lang.Exception {

		userType = JOptionPane.showInputDialog("Which user are you?\n[c] Client\n[o] Officer");
		identifier = JOptionPane.showInputDialog("Insert your userID (user1; user2; user3; user4; user5; user6)");

		String userId = (String) userType + identifier;


		// implement in this method

		if (userId.contains("c")) {

			String choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
					"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");

			while (true) {
				// 1- create claim
				if (choice.equals("1")) {
					String claimDescription = JOptionPane.showInputDialog("Write down your claim");
					int claimId = claimDataStore.createClaim(claimDescription, Integer.parseInt(identifier));
					String claimToString = claimDataStore.retrieveClaim(claimId);
					JOptionPane.showMessageDialog(null, "Claim created.\n" + claimToString + "\nPress 'OK' to continue");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
							"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
				}

				// 2- read claim
				else if (choice.equals("2")) {

					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					try {
						String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId));
						JOptionPane.showMessageDialog(null, claimToString + "\nPress 'OK' to continue.");
						choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
								"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
					} catch (ClaimNotFoundException_Exception e) {
						JOptionPane.showMessageDialog(null, "Claim " + claimId + " does not exist.\n");
						choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
								"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
					}
				}

				// 3- update claim
				else if (choice.equals("3")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");

					try {
						int uId = claimDataStore.getClaimUser((Integer.parseInt(claimId)));
						while (uId != Integer.parseInt(identifier))
							claimId = JOptionPane.showInputDialog("This claim belongs to another user.\n Please insert claim ID again:");
						String claimDescription = JOptionPane.showInputDialog("Insert the new claim description:");
						claimDataStore.updateClaim(Integer.parseInt(claimId), claimDescription);
						String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId));

						JOptionPane.showMessageDialog(null, "Claim updated.\n" + claimToString + "\nPress 'OK' to continue");

						choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
								"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
					} catch (ClaimNotFoundException_Exception e) {
						JOptionPane.showMessageDialog(null, "Claim " + claimId + " does not exist.\n");
						choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
								"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
					}
				}

				// 4 - submit document
				else if (choice.equals("4")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					int uId = claimDataStore.getClaimUser((Integer.parseInt(claimId)));

					while (uId != Integer.parseInt(identifier))
						claimId = JOptionPane.showInputDialog("This claim belongs to another user.\n Please insert claim ID again:");

					String docName = JOptionPane.showInputDialog("Insert document name:");
					String docContent = JOptionPane.showInputDialog("Insert document content :");

					Signature sign = new Signature();
					String encryptedHash = sign.createSignature("privateKeys\\" + "user" + identifier + "\\" + "user" + identifier + "PrivateKey", docContent);

					// only stores after signature validation
					boolean validation = claimDataStore.createDocument(Integer.parseInt(claimId), docName, docContent, identifier, encryptedHash);

					if (validation) {
						JOptionPane.showMessageDialog(null, "Successfull upload!\nPress 'OK' to continue.");
					} else {
						JOptionPane.showMessageDialog(null, "Error! Document was not successfully uploaded.\nPress 'OK' to continue.");
					}

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
							"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");

				}

				// 5 - read document
				else if (choice.equals("5")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					int uId = claimDataStore.getClaimUser((Integer.parseInt(claimId)));

					while (uId != Integer.parseInt(identifier))
						claimId = JOptionPane.showInputDialog("This claim belongs to another user.\n Please insert claim ID again:");

					String docId = JOptionPane.showInputDialog("Insert document ID:");

					String docToString = claimDataStore.readDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

					Document document = claimDataStore.getDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

					String encryptedHash = document.getSignature();
					Signature sign = new Signature();
					boolean validation = sign.validateSignature("publicKeys\\" + "user" + identifier + "PublicKey", encryptedHash, document.getContent());

					if (validation)
						JOptionPane.showMessageDialog(null, docToString + "\nThis document was not tampered!\n" + "\nPress 'OK' to continue.");
					else
						JOptionPane.showMessageDialog(null, docToString + "\nThis document was tampered!\n" + "\nPress 'OK' to continue.");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
							"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");

				}

				// 6 - update document
				else if (choice.equals("6")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					int uId = claimDataStore.getClaimUser((Integer.parseInt(claimId)));

					while (uId != Integer.parseInt(identifier))
						claimId = JOptionPane.showInputDialog("This claim belongs to another user.\n Please insert claim ID again:");

					String docId = JOptionPane.showInputDialog("Insert document ID:");
					String docContent = JOptionPane.showInputDialog("Insert new document content:");

					claimDataStore.updateDocument(identifier, Integer.parseInt(claimId), Integer.parseInt(docId), docContent);
					String docToString = claimDataStore.readDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

					JOptionPane.showMessageDialog(null, "Document updated.\n" + docToString + "\nPress 'OK' to continue");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
							"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");

				}

				// 7 - delete document
				else if (choice.equals("7")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");

					int uId = claimDataStore.getClaimUser((Integer.parseInt(identifier)));

					while (uId != Integer.parseInt(userId))
						claimId = JOptionPane.showInputDialog("This claim belongs to another user.\n Please insert claim ID again:");

					String docId = JOptionPane.showInputDialog("Insert document ID:");

					claimDataStore.deleteDocument(identifier, Integer.parseInt(claimId), Integer.parseInt(docId));

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
							"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
				}

				// 8 - List documents
				else if (choice.equals("8")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					int uId = claimDataStore.getClaimUser((Integer.parseInt(claimId)));

					while (uId != Integer.parseInt(identifier))
						claimId = JOptionPane.showInputDialog("This claim belongs to another user.\n Please insert claim ID again:");

					Claim claim = claimDataStore.getClaim(Integer.parseInt(claimId));
					/*for (int i = 1; i < claimDataStore.getNumberOfDocs(Integer.parseInt(claimId)); i++) {
						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), i);
						String encryptedHash = document.getSignature();
						Signature sign = new Signature();
						boolean validation = sign.validateSignature("publicKeys\\" + "user" + identifier + "PublicKey", encryptedHash, document.getContent());
					}*/
					JOptionPane.showMessageDialog(null, claimDataStore.listDocuments(Integer.parseInt(claimId)).toString());
					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
							"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n  [8] - List Documents\n exit to finish");
				} else {
					break;
				}
			}

		} else if (userId.contains("o")) {

			String choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
					"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");

			while (true) {

				if (choice.equals("1")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");

					String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId));
					JOptionPane.showMessageDialog(null, claimToString + "\nPress 'OK' to continue.");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");

				} else if (choice.equals("2")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					String claimDescription = JOptionPane.showInputDialog("Insert the new claim description:");

					claimDataStore.updateClaim(Integer.parseInt(claimId), claimDescription);
					String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId));

					JOptionPane.showMessageDialog(null, "Claim updated.\n" + claimToString + "\nPress 'OK' to continue");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");

				} else if (choice.equals("3")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					String docName = JOptionPane.showInputDialog("Insert document name:");
					String docContent = JOptionPane.showInputDialog("Insert document content :");

					Signature sign = new Signature();
					String encryptedHash = sign.createSignature("privateKeys\\" + "user" + identifier + "\\" + "user" + identifier + "PrivateKey", docContent);
					//so guarda o document apos validacao
					boolean validation = claimDataStore.createDocument(Integer.parseInt(claimId), docName, docContent, identifier, encryptedHash);

					if (validation) {
						JOptionPane.showMessageDialog(null, "Successfull upload!\nPress 'OK' to continue.");
					} else {
						JOptionPane.showMessageDialog(null, "Error! Document was not successfully uploaded.\nPress 'OK' to continue.");
					}

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");

				} else if (choice.equals("4")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					String docId = JOptionPane.showInputDialog("Insert document ID:");

					String docToString = claimDataStore.readDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

					Document document = claimDataStore.getDocument(Integer.parseInt(claimId), Integer.parseInt(docId));
					String encryptedHash = document.getSignature();
					Signature sign = new Signature();

					boolean validation = sign.validateSignature("publicKeys\\" + "user" + identifier + "PublicKey", encryptedHash, document.getContent());
					JOptionPane.showMessageDialog(null, docToString + "\nPress 'OK' to continue");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");

				} else if (choice.equals("5")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					String docId = JOptionPane.showInputDialog("Insert document ID:");
					String docContent = JOptionPane.showInputDialog("Insert new document content:");

					claimDataStore.updateDocument(identifier, Integer.parseInt(claimId), Integer.parseInt(docId), docContent);
					String docToString = claimDataStore.readDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

					JOptionPane.showMessageDialog(null, "Document updated.\n" + docToString + "\nPress 'OK' to continue");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");

				} else if (choice.equals("6")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					String docId = JOptionPane.showInputDialog("Insert document ID:");

					claimDataStore.deleteDocument(identifier, Integer.parseInt(claimId), Integer.parseInt(docId));

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");
				} else if (choice.equals("7")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					Claim claim = claimDataStore.getClaim(Integer.parseInt(claimId));
					/*for (int i = 1; i < claimDataStore.getNumberOfDocs(Integer.parseInt(claimId)); i++) {
						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), i);
						String encryptedHash = document.getSignature();
						Signature sign = new Signature();
						boolean validation = sign.validateSignature("publicKeys\\" + "user" + identifier + "PublicKey", encryptedHash, document.getContent());
					}*/

					JOptionPane.showMessageDialog(null, claimDataStore.listDocuments(Integer.parseInt(claimId)).toString());
					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n  [7] - List Documents\n exit to finish");
				} else {
					break;
				}
			} //while(true)

		} // if userId.contains("o")


	} //runClaimDataBase


}

