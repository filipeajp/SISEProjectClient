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
	public static void main (String[] args) throws InterruptedException, ClaimNotFoundException_Exception, Exception_Exception {
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
	public static void runClaimDataBase (ClaimDataStore claimDataStore, String identifier) throws ClaimNotFoundException_Exception, Exception_Exception {

		// implement in this method

		if (identifier.equals("Client")) {

			String choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");

			while (true) {
				if (choice.equals("1")) {
					String claim_desc = JOptionPane.showInputDialog("Write down your claim");
					claimDataStore.createClaim(claim_desc);
					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");
				} else if (choice.equals("2")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					String docName = JOptionPane.showInputDialog("Insert document name:");
					String docContent = JOptionPane.showInputDialog("Insert document content :");

					claimDataStore.createDocument(Integer.parseInt(claimId), docName, docContent, "sdf", "user1PrivateKey");
					choice = JOptionPane.showInputDialog("Thank you for using our service. Select one of the following options \n [1] - Create a Claim \n [2] - Summit Documents\n [3] - List Documents \n  exit to finish");
				} else if (choice.equals("3")) {
					String claimId = JOptionPane.showInputDialog("Insert claim ID:");
					Claim claim = claimDataStore.getClaim(Integer.parseInt(claimId));

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

