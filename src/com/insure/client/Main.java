package com.insure.client;
//  Criar Servidor
// Mudar a dir to script
// Correr no Terminal Wsimport.bat -s ..\src -keep -p com.insure.client http://localhost:8090/docstorage?wsdl

import javax.swing.*;
import javax.xml.ws.BindingProvider;

public class Main {
	private static final String identifier = "Client";

	/*
	 * The main function initialize the remote instance and connects to the remote service
	 * */
	public static void main (String[] args) throws java.lang.Exception {
		ClaimDataStoreService claimdatastoreService = new ClaimDataStoreService();

		ClaimDataStore claimDataStore = (ClaimDataStore) claimdatastoreService.getClaimDataStorePort();

		((BindingProvider) claimDataStore).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"http://localhost:8090/docstorage");

		runClaimDataBase(claimDataStore, identifier);

	}

	/*
	 * This method executes the ClaimData client by receiving an expression as input
	 * from the user with numbers, multiplications and summations and printing the result
	 * ClaimDataStore will run until the user input 'exit'
	 * */
	public static void runClaimDataBase (ClaimDataStore claimDataStore, String identifier) throws java.lang.Exception {
		identifier = JOptionPane.showInputDialog("Insert your userID (user1; user2; user3; user4; user5; user6)");


		// implement in this method

		if (identifier.equals("1")) {

			String choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");

			while (true) {
				if (choice.equals("1")) {
					String claimDescription = JOptionPane.showInputDialog("Write down your claim");
					int claimId = claimDataStore.createClaim(claimDescription);
					String claimToString = claimDataStore.retrieveClaim(claimId);
					JOptionPane.showMessageDialog(null, "Claim created.\n" + claimToString + "\nPress 'OK' to continue");

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");

				} else if (choice.equals("2")) {
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
					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");

				} else if (choice.equals("3")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					Claim claim = claimDataStore.getClaim(Integer.parseInt(claimId));
					/*for (int i = 1; i < claimDataStore.getNumberOfDocs(Integer.parseInt(claimId)); i++) {
						Document document = claimDataStore.getDocument(Integer.parseInt(claimId), i);
						String encryptedHash = document.getSignature();
						Signature sign = new Signature();
						boolean validation = sign.validateSignature("publicKeys\\" + "user" + identifier + "PublicKey", encryptedHash, document.getContent());
					}*/


					JOptionPane.showMessageDialog(null, claimDataStore.listDocuments("user1", Integer.parseInt(claimId)).toString());

					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");

				} else {
					break;
				}
			}
		} else if (identifier.equals("Officer")) {

			String choice = JOptionPane.showInputDialog("Ready to work. Select one of the following options \n [1] - Summit Documents\n [2] - List Documents \n  exit to finish");

			while (true) {

				if (choice.equals("1")) {

				} else if (choice.equals("2")) {
					String claim_desc = JOptionPane.showInputDialog("Document");


				} else {
					System.out.println("Exiting");
					break;
				}
			}


		}


	}


}

