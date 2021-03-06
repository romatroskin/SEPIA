package de.uni.freiburg.iig.telematik.sepia.parser.graphic;

import de.invation.code.toval.parser.ParserException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsing;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsingFormat;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import java.io.IOException;



public class PNParserDialog extends JDialog {

	private static final long serialVersionUID = -1556400321094068143L;

	private JTextField formatBox = null;
	private JTextField pnPathField = null;
	private AbstractGraphicalPN petriNet = null;
	private JButton browseButton = null;
	private JButton okButton = null;
	private JPanel panelButtons = null;
	private JPanel panelInput = null;
	private final JPanel centerPanel = new JPanel(new BorderLayout());
	private final JPanel parameterPanel = new JPanel(new BorderLayout());
	private final PNMLParameterPanel pnmlParameterPanel = new PNMLParameterPanel();
	private final SilentTransitionPanel transitionPanel = new SilentTransitionPanel();
	

	/**
	 * Create the dialog.
	 */
	private PNParserDialog(Window owner) {
		super(owner);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	
		setTitle("Choose Petri net");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getInputPanel(), BorderLayout.PAGE_START);
		getContentPane().add(getButtonPanel(), BorderLayout.PAGE_END);
		centerPanel.add(parameterPanel, BorderLayout.PAGE_START);
		centerPanel.add(transitionPanel, BorderLayout.CENTER);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		adjustParameterPanel();
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				petriNet = null;
			}
			
		});
	}
	
	private JPanel getInputPanel(){
		if(panelInput == null){
			panelInput = new JPanel();
			panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			GroupLayout layout = new GroupLayout(panelInput);
			panelInput.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			SequentialGroup horizGroup = layout.createSequentialGroup();
			JLabel inputFileLabel = new JLabel("Input file:");
			inputFileLabel.setPreferredSize(new Dimension(100,30));
			inputFileLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			JLabel inputFormatLabel = new JLabel("Input format:");
			inputFormatLabel.setPreferredSize(new Dimension(100,30));
			inputFormatLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			horizGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(inputFileLabel).addComponent(inputFormatLabel));
			horizGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(getPNPathField()).addComponent(getFormatBox()));
			horizGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(getBrowseButton()));
			layout.setHorizontalGroup(horizGroup);
			
			SequentialGroup verticalGroup = layout.createSequentialGroup();
			verticalGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(inputFileLabel).addComponent(getPNPathField()).addComponent(getBrowseButton()));
			verticalGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(inputFormatLabel).addComponent(getFormatBox()));
			layout.setVerticalGroup(verticalGroup);
		}
		return panelInput;
	}
	
	private JPanel getButtonPanel(){
		if(panelButtons == null) {
			panelButtons= new JPanel();
			panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
			panelButtons.add(getOKButton());
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					petriNet = null;
					dispose();
				}
			});
			panelButtons.add(cancelButton);
		}
		return panelButtons;
	}
	
	private JButton getOKButton(){
		if(okButton == null){
			okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(petriNet == null){
						JOptionPane.showMessageDialog(PNParserDialog.this, "Please parse a Petri net first.", "Parameter Exception", JOptionPane.ERROR_MESSAGE);
						return;
					}
					for(String silentTransition: transitionPanel.getSilentTransitions()){
						petriNet.getPetriNet().getTransition(silentTransition).setSilent(true);
					}
					dispose();
				}
			});
		}
		return okButton;
	}
	
	private void adjustParameterPanel(){
		parameterPanel.removeAll();
		switch(getFormatBox().getText()){
		case "Petrifiy":
			break;
		case "PNML":
			parameterPanel.add(pnmlParameterPanel, BorderLayout.CENTER);
			break;
		}
	}
	
	private JButton getBrowseButton(){
		if(browseButton == null){
			browseButton = new JButton("Browse...");
			browseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PNChooser pnChooser = new PNChooser();
					int returnVal = pnChooser.showOpenDialog(PNParserDialog.this);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = pnChooser.getSelectedFile();
			            getPNPathField().setText(file.getAbsolutePath());
						getFormatBox().setText(PNParsing.guessFormat(file).toString());
						getFormatBox().setEnabled(false);
						getFormatBox().setVisible(true);
						
						// parse
						if(getPNFileName() == null || getPNFileName().isEmpty()){
							JOptionPane.showMessageDialog(PNParserDialog.this, "Please choose an input file first!", "Parameter Exception", JOptionPane.ERROR_MESSAGE);
							return;
						}
						switch(getParsingFormat()){
						case PETRIFY:
							//TODO
							break;
						case PNML:
							PNMLParser parser = new PNMLParser();
							getPNFileName();
							try {
								petriNet = parser.parse(getPNFileName(), pnmlParameterPanel.requireNetType(), pnmlParameterPanel.validation());
								transitionPanel.update(petriNet.getPetriNet());
							} catch (IOException | ParserException ex) {
								JOptionPane.showMessageDialog(PNParserDialog.this, "Exception in parsing procedure:\nReason: "+ex.getMessage(), "Parsing Exception", JOptionPane.ERROR_MESSAGE);						
							}
							break;
						}
			        }
				}
			});
		}
		return browseButton;
	}
		
	private String getPNFileName(){
		return getPNPathField().getText();
	}
	
	private JTextField getPNPathField(){
		if(pnPathField == null){
			pnPathField = new JTextField();
			pnPathField.setPreferredSize(new Dimension(200,30));
		}
		return pnPathField;
	}

	private JTextField getFormatBox(){
		if(formatBox == null){
			formatBox = new JTextField("Petrify, PNML");
		}
		return formatBox;
	}
	
	private PNParsingFormat getParsingFormat(){
		return PNParsingFormat.valueOf(getFormatBox().getText());
	}
	
	@SuppressWarnings("rawtypes")
	public AbstractGraphicalPN getPetriNet(){
		return petriNet;
	}
	
	@SuppressWarnings("rawtypes")
	public static AbstractGraphicalPN showPetriNetDialog(Window parentWindow){
		PNParserDialog dialog = new PNParserDialog(parentWindow);
		return dialog.getPetriNet();
	}
		
}
