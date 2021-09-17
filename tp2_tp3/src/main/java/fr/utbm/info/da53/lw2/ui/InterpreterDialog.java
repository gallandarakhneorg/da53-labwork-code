/* 
 * $Id$
 * 
 * Copyright (c) 2012-2021 Stephane GALLAND.
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.utbm.info.da53.lw2.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import fr.utbm.info.da53.lw2.context.DebugInterpreter;
import fr.utbm.info.da53.lw2.context.Interpreter;
import fr.utbm.info.da53.lw2.context.InterpreterListener;
import fr.utbm.info.da53.lw2.context.StandardInput;
import fr.utbm.info.da53.lw2.context.StandardOutput;
import fr.utbm.info.da53.lw2.context.Statement;
import fr.utbm.info.da53.lw2.error.ErrorRepository;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Windows that permits to debug the interpreted program.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 */
public class InterpreterDialog extends JFrame implements ActionListener, InterpreterListener {

	private static final long serialVersionUID = -4427922779931171532L;
	
	private static final String SIGN = "#>"; //$NON-NLS-1$
	private static final String NO_SIGN = "  "; //$NON-NLS-1$
	
	/** Debug context.
	 */
	final DebugInterpreter context;
	private boolean freeze = false;
	
	private final JButton[] buttons = new JButton[3];
	private final JTable code;
	private final DefaultTableModel symbolTable;
	private final JTextArea errorConsole;
	private final JTextArea stdoutConsole;

	/**
	 * @param title
	 * @param context
	 * @param enableRun
	 */
	public InterpreterDialog(String title, DebugInterpreter context, boolean enableRun) {
		super(title);
		this.context = context;

		JScrollPane sp, sp2;
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel tools = new JPanel();
		tools.setLayout(new BoxLayout(tools, BoxLayout.X_AXIS));
		add(tools, BorderLayout.NORTH);

		this.buttons[0] = new JButton("Step"); //$NON-NLS-1$
		this.buttons[0].setActionCommand("STEP"); //$NON-NLS-1$
		this.buttons[0].addActionListener(this);
		tools.add(this.buttons[0]);

		this.buttons[1] = new JButton("Run to Line"); //$NON-NLS-1$
		this.buttons[1].setActionCommand("LINE"); //$NON-NLS-1$
		this.buttons[1].addActionListener(this);
		tools.add(this.buttons[1]);

		this.buttons[2] = new JButton("Run to End"); //$NON-NLS-1$
		this.buttons[2].setActionCommand("END"); //$NON-NLS-1$
		this.buttons[2].addActionListener(this);
		tools.add(this.buttons[2]);

		DefaultTableModel model = new DefaultTableModel(0, 3);
		this.code = new JTable(model);
		this.code.getColumnModel().getColumn(0).setHeaderValue("CS"); //$NON-NLS-1$
		this.code.getColumnModel().getColumn(0).setMaxWidth(30);
		this.code.getColumnModel().getColumn(0).setResizable(false);
		this.code.getColumnModel().getColumn(1).setHeaderValue("Lines"); //$NON-NLS-1$
		this.code.getColumnModel().getColumn(1).setMaxWidth(100);
		this.code.getColumnModel().getColumn(2).setHeaderValue("Statements"); //$NON-NLS-1$
		sp = new JScrollPane(this.code);
		sp.setPreferredSize(new Dimension(400, 200));
		
		if (this.context!=null) {
			SortedMap<Integer,Statement> code = this.context.getCode();
			for(Entry<Integer,Statement> entry : code.entrySet()) {
				model.addRow(new Object[] { NO_SIGN, entry.getKey(), entry.getValue() });
			}
		}
		
		this.symbolTable = new DefaultTableModel(0, 3);
		JTable st = new JTable(this.symbolTable);
		st.getColumnModel().getColumn(0).setHeaderValue("Variables"); //$NON-NLS-1$
		st.getColumnModel().getColumn(1).setHeaderValue("Types"); //$NON-NLS-1$
		st.getColumnModel().getColumn(2).setHeaderValue("Values"); //$NON-NLS-1$
		sp2 = new JScrollPane(st);

		JSplitPane debugPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, sp2);

		
		JPanel consoles = new JPanel(new GridLayout(1, 2));
		add(consoles, BorderLayout.SOUTH);
		
		this.errorConsole = new JTextArea();
		sp = new JScrollPane(this.errorConsole);
		consoles.add(sp);

		this.stdoutConsole = new JTextArea();
		this.stdoutConsole.append("Standard Console:\n"); //$NON-NLS-1$
		sp = new JScrollPane(this.stdoutConsole);
		sp.setPreferredSize(new Dimension(200, 200));
		consoles.add(sp);

		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, debugPanel, consoles), BorderLayout.CENTER);
		
		refreshContent();
		
		if (enableRun) enableGUI();
		else disableGUI();
		
		if (this.context!=null) {
			this.context.getRootContext().getInterpreter().setStandardInput(new StdIn());
			this.context.getRootContext().getInterpreter().setStandardOutput(new StdOut());
			this.context.getRootContext().getInterpreter().addInterpreterListener(this);
		}

		pack();
	}
	
	/** Replies the stdout console.
	 * 
	 * @return the stdout console.
	 */
	JTextArea getStdoutConsole() {
		return this.stdoutConsole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpreterKilled(Interpreter interpreter) {
		this.freeze = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("STEP".equals(e.getActionCommand())) { //$NON-NLS-1$
			disableGUI();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						InterpreterDialog.this.context.step();
						enableGUI();
					}
					catch (InterpreterException e1) {
						ErrorRepository.add(e1);
						disableGUI();
					}
					refreshContent();
				}
			});
		}
		else if ("LINE".equals(e.getActionCommand())) { //$NON-NLS-1$
			disableGUI();
			String input = JOptionPane.showInputDialog(this, "Enter a line number"); //$NON-NLS-1$
			Integer line = NumberUtil.parseInt(input);
			if (line!=null) {
				SwingUtilities.invokeLater(new LineRunner(line.intValue()));
			}
		}
		else if ("END".equals(e.getActionCommand())) { //$NON-NLS-1$
			disableGUI();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						InterpreterDialog.this.context.runToEnd();
						enableGUI();
					}
					catch (InterpreterException e1) {
						ErrorRepository.add(e1);
						disableGUI();
					}
					refreshContent();
				}
			});
		}
	}

	/** Enable the UI components.
	 */
	void enableGUI() {
		for(JButton bt : this.buttons) {
			bt.setEnabled(!this.freeze);
		}
	}
	
	/** Disable the UI components.
	 */
	void disableGUI() {
		for(JButton bt : this.buttons) {
			bt.setEnabled(false);
		}
	}
	
	/** Refresh the content of the debugger
	 */
	void refreshContent() {
		int basicLine = -1; 
		if (this.context!=null && this.context.getCurrentContext()!=null) {
			basicLine = this.context.getCurrentContext().getCurrentLine();
		}
		TableModel tm = this.code.getModel();
		int selectedRow = -1;
		for(int i=0; i<tm.getRowCount(); ++i) {
			if (basicLine == (Integer)tm.getValueAt(i, 1)) {
				tm.setValueAt(SIGN, i, 0);
				selectedRow = i;
			}
			else {
				tm.setValueAt(NO_SIGN, i, 0);
			}
		}
		this.code.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
		
		while(this.symbolTable.getRowCount()>0) {
			this.symbolTable.removeRow(0);
		}
		
		if (this.context!=null && this.context.getCurrentContext()!=null) {
			for(SymbolTableEntry entry : this.context.getCurrentContext().snapshot()) {
				Value value = entry.getValue();
				VariableType type = value.getType();
				this.symbolTable.addRow(new Object[] {
						entry.id(),
						(type==null ? "undef" : type), //$NON-NLS-1$
						value
				});
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ps.println("Error Console:"); //$NON-NLS-1$
		ErrorRepository.print(ps);
		try {
			baos.close();
		}
		catch (IOException e) {
			//
		}
		this.errorConsole.setText(new String(baos.toByteArray()));
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 */
	private class LineRunner implements Runnable {

		private final int line;
		
		public LineRunner(int line) {
			this.line = line;
		}
		
		@Override
		public void run() {
			try {
				InterpreterDialog.this.context.runUntilLine(this.line);
				enableGUI();
			}
			catch (InterpreterException e1) {
				ErrorRepository.add(e1);
				disableGUI();
			}
			refreshContent();
		}
		
	}
	
	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 */
	private class StdIn implements StandardInput {

		/**
		 */
		public StdIn() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Value readString(String message) {
			String line = JOptionPane.showInputDialog(
					InterpreterDialog.this, message);
			return Value.parseValue(line);
		}
		
	}
	
	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 */
	private class StdOut implements StandardOutput {

		/**
		 */
		public StdOut() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void println(String message) {
			JTextArea console = getStdoutConsole();
			console.append(message);
			console.append("\n"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void print(String message) {
			JTextArea console = getStdoutConsole();
			console.append(message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void println() {
			JTextArea console = getStdoutConsole();
			console.append("\n"); //$NON-NLS-1$
		}
		
	}

}
