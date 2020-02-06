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
		String[] userOptions = {"Client", "Officer"};
		String[] clientIdOptions = {"1", "2", "3", "4", "5", "6"};
		String[] officerIdOptions = {"7", "8", "9", "10"};

		ImageIcon logo = new ImageIcon("insure.png");

		int i = JOptionPane.showOptionDialog(null, "Which user are you?", "User type",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
				logo, userOptions, userOptions[0]);

		userType = userOptions[i];

		if (userType.equals("Client")) {
			i = JOptionPane.showOptionDialog(null, "Insert your clientID.", "Client ID",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					logo, clientIdOptions, clientIdOptions[0]);
			identifier = clientIdOptions[i];
		} else {
			i = JOptionPane.showOptionDialog(null, "Insert your officerID.", "Officer ID",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					logo, officerIdOptions, officerIdOptions[0]);
			identifier = clientIdOptions[i];
		}

		String userId = (String) userType + identifier;

		// if user is an insurer
		if (userId.contains("Client")) {

			while (true) {
				String choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Create a Claim\n [2] - Read Claim\n " +
						"[3] - Update claim\n [4] - Submit Document\n [5] - Read Document\n [6] - Update Document\n [7] - Delete Document\n [8] - List Documents\n [9] - Simulate document tampering\n exit to finish");
				try {

					// 1- create claim
					if (choice.equals("1")) {
						String claimDescription = JOptionPane.showInputDialog("Write down your claim");
						int claimId = claimDataStore.createClaim(claimDescription, Integer.parseInt(identifier));
						String claimToString = claimDataStore.retrieveClaim(claimId, Integer.parseInt(identifier));
						JOptionPane.showMessageDialog(null, "Claim created.\n" + claimToString + "\nPress 'OK' to continue");
					}

					// 2- read claim
					else if (choice.equals("2")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId), Integer.parseInt(identifier));
						JOptionPane.showMessageDialog(null, claimToString + "\nPress 'OK' to continue.");
					}

					// 3- update claim
					else if (choice.equals("3")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");


						String claimDescription = JOptionPane.showInputDialog("Insert the new claim description:");

						claimDataStore.updateClaim(Integer.parseInt(identifier), Integer.parseInt(claimId), claimDescription);
						String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId), Integer.parseInt(identifier));

						JOptionPane.showMessageDialog(null, "Claim updated.\n" + claimToString + "\nPress 'OK' to continue");
					}

					// 4 - submit document
					else if (choice.equals("4")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String docName = JOptionPane.showInputDialog("Insert document name:");
						String docContent = JOptionPane.showInputDialog("Insert document content :");

						Signature sign = new Signature();
						String encryptedHash = sign.createSignature("privateKeys\\" + "user" + identifier + "\\" + "user" + identifier + "PrivateKey", docContent);

						// only stores after signature validation
						claimDataStore.createDocument(Integer.parseInt(claimId), docName, docContent, Integer.parseInt(identifier), Integer.parseInt(identifier), encryptedHash);

					}

					// 5 - read document
					else if (choice.equals("5")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");

						String docToString = claimDataStore.readDocument(Integer.parseInt(identifier), Integer.parseInt(claimId), Integer.parseInt(docId));

						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

						int docOwnerId = claimDataStore.getDocumentOwner(Integer.parseInt(claimId), Integer.parseInt(docId));

						String encryptedHash = document.getSignature();
						Signature sign = new Signature();
						sign.validateSignature("publicKeys\\" + "user" + docOwnerId + "PublicKey", encryptedHash, document.getContent());

						JOptionPane.showMessageDialog(null, docToString);
					}

					// 6 - update document
					else if (choice.equals("6")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");

						String docContent = JOptionPane.showInputDialog("Insert new document content:");

						claimDataStore.updateDocument(Integer.parseInt(identifier), Integer.parseInt(claimId), Integer.parseInt(docId), docContent);
						String docToString = claimDataStore.readDocument(Integer.parseInt(identifier), Integer.parseInt(claimId), Integer.parseInt(docId));

						JOptionPane.showMessageDialog(null, "Document updated.\n" + docToString + "\nPress 'OK' to continue");
					}

					// 7 - delete document
					else if (choice.equals("7")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");

						claimDataStore.deleteDocument(Integer.parseInt(identifier), Integer.parseInt(claimId), Integer.parseInt(docId));
					}

					// 8 - List documents
					else if (choice.equals("8")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						Claim claim = claimDataStore.getClaim(Integer.parseInt(claimId));

						JOptionPane.showMessageDialog(null, claimDataStore.listDocuments(Integer.parseInt(identifier), Integer.parseInt(claimId)).toString());

					}

					// 9 - simulate document tampering
					else if (choice.equals("9")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");

						String docContent = JOptionPane.showInputDialog("Insert new document content:");

						claimDataStore.simulateTampering(Integer.parseInt(claimId), Integer.parseInt(docId), docContent);

						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), Integer.parseInt(docId));
						String encryptedHash = document.getSignature();
						int documentOwnerId = claimDataStore.getDocumentOwner(Integer.parseInt(claimId), Integer.parseInt(docId));

						Signature sign = new Signature();

						sign.validateSignature("publicKeys\\" + "user" + documentOwnerId + "PublicKey", encryptedHash, docContent);
					}

					// exit to close application
					else if (choice.equals("exit")) {
						break;
					} else {
						JOptionPane.showMessageDialog(null, "Unknown command.\nPress 'OK' to restart.");
					}

				} catch (ClaimNotFoundException_Exception | DocumentNotFoundException_Exception | WrongUserIdException_Exception | TamperedDocumentException_Exception | NumberFormatException e) {

					JOptionPane.showMessageDialog(null, e.getMessage());

				}
			} //while(true)
		} // if user is a client

		// if user is a Officer
		else if (userId.contains("Officer")) {

			while (true) {

				try {

					String choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options\n [1] - Read Claim\n " +
							"[2] - Update claim\n [3] - Submit Document\n [4] - Read Document\n [5] - Update Document\n [6] - Delete Document\n [7] - List Documents\n [8] - Simulate document tampering\n exit to finish");

					// 1 - read claim
					if (choice.equals("1")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId), clientId);
						JOptionPane.showMessageDialog(null, claimToString + "\nPress 'OK' to continue.");
					}

					// 	2 - update claim
					else if (choice.equals("2")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						String claimDescription = JOptionPane.showInputDialog("Insert the new claim description:");

						claimDataStore.updateClaim(clientId, Integer.parseInt(claimId), claimDescription);
						String claimToString = claimDataStore.retrieveClaim(Integer.parseInt(claimId), clientId);

						JOptionPane.showMessageDialog(null, "Claim updated.\n" + claimToString + "\nPress 'OK' to continue");
					}

					// 3 - submit document
					else if (choice.equals("3")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						String docName = JOptionPane.showInputDialog("Insert document name:");
						String docContent = JOptionPane.showInputDialog("Insert document content :");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						Signature sign = new Signature();
						String encryptedHash = sign.createSignature("privateKeys\\" + "user" + identifier + "\\" + "user" + identifier + "PrivateKey", docContent);
						//so guarda o document apos validacao
						claimDataStore.createDocument(Integer.parseInt(claimId), docName, docContent, clientId, Integer.parseInt(identifier), encryptedHash);

						JOptionPane.showMessageDialog(null, "Successfull upload!\nPress 'OK' to continue.");

					}

					// 4 - read document
					else if (choice.equals("4")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");

						String docToString = claimDataStore.readDocument(clientId, Integer.parseInt(claimId), Integer.parseInt(docId));

						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), Integer.parseInt(docId));

						int docOwnerId = claimDataStore.getDocumentOwner(clientId, Integer.parseInt(docId));

						String encryptedHash = document.getSignature();
						Signature sign = new Signature();

						sign.validateSignature("publicKeys\\" + "user" + docOwnerId + "PublicKey", encryptedHash, document.getContent());

						JOptionPane.showMessageDialog(null, docToString + "\nPress 'OK' to continue");
					}

					// 5 - update document
					else if (choice.equals("5")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");
						String docContent = JOptionPane.showInputDialog("Insert new document content:");

						claimDataStore.updateDocument(clientId, Integer.parseInt(claimId), Integer.parseInt(docId), docContent);
						String docToString = claimDataStore.readDocument(clientId, Integer.parseInt(claimId), Integer.parseInt(docId));

						JOptionPane.showMessageDialog(null, "Document updated.\n" + docToString + "\nPress 'OK' to continue");
					}

					// 6 - delete document
					else if (choice.equals("6")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");

						claimDataStore.deleteDocument(clientId, Integer.parseInt(claimId), Integer.parseInt(docId));
					}

					// 7 - list documents
					else if (choice.equals("7")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");

						int clientId = claimDataStore.getClaimUser(Integer.parseInt(claimId));

						Claim claim = claimDataStore.getClaim(Integer.parseInt(claimId));

						JOptionPane.showMessageDialog(null, claimDataStore.listDocuments(clientId, Integer.parseInt(claimId)).toString());
					}

					// 8 - simulate tampered document
					else if (choice.equals("8")) {
						String claimId = JOptionPane.showInputDialog("Insert claim ID (positive integer number): ");
						String docId = JOptionPane.showInputDialog("Insert document ID (positive integer number): ");
						String docContent = JOptionPane.showInputDialog("Insert new document content:");

						claimDataStore.simulateTampering(Integer.parseInt(claimId), Integer.parseInt(docId), docContent);

						Signature sign = new Signature();
						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), Integer.parseInt(docId));
						String encryptedHash = document.getSignature();

						sign.validateSignature("publicKeys\\" + "user" + userId + "PublicKey", encryptedHash, docContent);
					}

					// exit to close application
					else if (choice.equals("exit")) {
						break;
					} else {
						JOptionPane.showMessageDialog(null, "Unknown command.\nPress 'OK' to restart.");
					}

				} catch (ClaimNotFoundException_Exception | DocumentNotFoundException_Exception | WrongUserIdException_Exception | TamperedDocumentException_Exception | NumberFormatException e) {

					JOptionPane.showMessageDialog(null, e.getMessage());
				}

			} //while(true)

		} // if user is an officer


	} //runClaimDataBase


}

