package com.oencue.client;

import com.oencue.shared.FieldVerifier;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hello implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	//private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final noteMapperAsync noteMapper = GWT.create(noteMapper.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Login");
		final Button newNoteButton = new Button("New Note");
		final TextBox nameField = new TextBox();
		nameField.setText("Your Username");
		final Label errorLabel = new Label();
		
		final TextBox nameField2 = new TextBox();
		nameField2.setText("List of previous Notes");
		
		final TextArea newNoteField = new TextArea();
		newNoteField.setText("Type New Note here\nThen press save");
		final Button saveButton = new Button("Save");

		// We can add style names to widgets
		//sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel.get("nameFieldContainer").add(nameField);
		//RootPanel.get("sendButtonContainer").add(sendButton);
		//RootPanel.get("errorLabelContainer").add(errorLabel);
		
		final VerticalPanel panel1 = new VerticalPanel();
		final VerticalPanel panel2 = new VerticalPanel();
		final VerticalPanel panel3 = new VerticalPanel();
		
		class updatePanels {
		public void updatePanel(VerticalPanel panel) {
			panel.setWidth("100%");
			panel.setHeight("100%");
			panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
		}
		}
		updatePanels updater = new updatePanels();
		updater.updatePanel(panel1);
		updater.updatePanel(panel2);
		updater.updatePanel(panel3);

		panel1.add(new HTML("<b>Enter your name:</b>"));
		sendButton.getElement().setClassName("button");
		nameField.getElement().setClassName("textBox");

		panel1.add(nameField);
		panel1.add(sendButton);
		panel1.add(errorLabel);

		newNoteButton.getElement().setClassName("button");
		nameField2.getElement().setClassName("textBox");
		panel2.add(newNoteButton);
		panel2.add(nameField2);
		
		newNoteField.getElement().setClassName("textBox");
		saveButton.getElement().setClassName("button");
		panel3.add(newNoteField);
		panel3.add(saveButton);

		RootPanel.get("mainContainer").add(panel1);
		RootPanel.get("mainContainer").add(panel2);
		RootPanel.get("mainContainer").add(panel3);
		panel2.setVisible(false);
		panel3.setVisible(false);
		
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dialogVPanel.add(closeButton);
		dialogVPanel.setWidth("500px");
		dialogBox.setWidget(dialogVPanel);
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Add a handler to newNoteButton
		newNoteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				changeView();
			}
			
			private void changeView() {
				newNoteButton.setEnabled(true);
				newNoteButton.setFocus(true);
				dialogBox.hide();
				panel2.setVisible(false);
				panel3.setVisible(true);
				
			}
		});
		
		// Add a handler to saveButton
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
	
				changeView();
			}
			
			private void changeView() {
				
				dialogBox.setText("Data is Saved");
				serverResponseLabel.removeStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML("");
				textToServerLabel.setText("");
				dialogBox.center();
				closeButton.setFocus(true);
				
				saveButton.setEnabled(true);
				saveButton.setFocus(true);
				panel3.setVisible(false);
				panel2.setVisible(true);
			}
		});
		
		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				
				int command = 0;
				noteMapper.access2Database(textToServer,command, new AsyncCallback<String>() {
				//greetingService.greetServer(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						//dialogBox.setText("Remote Procedure Call - Failure");
						//serverResponseLabel.addStyleName("serverResponseLabelError");
						//serverResponseLabel.setHTML(SERVER_ERROR);
						//dialogBox.center();
						//closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result);
						dialogBox.center();
									
						closeButton.setFocus(true);
						panel1.setVisible(false);
						panel2.setVisible(true);
						

					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
