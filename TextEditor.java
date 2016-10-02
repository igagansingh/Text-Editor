import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class TextEditor implements ActionListener
{
	JFrame mainFrame;
	JPanel editPanel,logPanel;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;
	KeyStroke  i;
	JTabbedPane tabbedPane;
	JFileChooser fileChooser;
	JTextArea textArea,logArea;
	JScrollPane textScroll;
	Runtime runtime;
	FileWriter fileWriter;
	PrintWriter print;
	JComboBox option;
	static int count;

	public TextEditor(String s)
	{
		mainFrame = new JFrame(s);

		editPanel = new JPanel(new GridLayout(1,1,0,0));

		menuBar = new JMenuBar();
		setMenu();
		mainFrame.setJMenuBar(menuBar);

		tabbedPane = new JTabbedPane();
		editPanel.add(tabbedPane);
		
		logPanel = new JPanel(new GridLayout(1,1,0,0));
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setText("Text Editor by Gagandeep Singh");
		logPanel.add(logArea);		

		mainFrame.add(editPanel);
		mainFrame.add(logPanel);
		mainFrame.setLayout(new GridLayout(2,1,0,1));
		mainFrame.setSize(800,800);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		runtime= Runtime.getRuntime();
		
		newFile();
	}
	public static void main(String[] args) 
	{
		new TextEditor("Text Editor by Gagandeep Singh");	
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("New File"))
		{
			newFile();
		}
		if(e.getActionCommand().equals("Open"))
		{
			openFile();
		}
		if(e.getActionCommand().equals("Save"))
		{
			saveFile();
		}
		if(e.getActionCommand().equals("Save As"))
		{
			saveAsFile();
		}
		if(e.getActionCommand().equals("Close File"))
		{
			closeFile();
		}
		if(e.getActionCommand().equals("Exit"))
		{
			System.exit(0);
		}
		if(e.getActionCommand().equals("Compile"))
		{
			compileFile();
		}
		if(e.getActionCommand().equals("Run"))
		{
			runFile();
		}
		if(e.getActionCommand().equals("Cut"))
		{
			cut();
		}
		if(e.getActionCommand().equals("Copy"))
		{
			copy();
		}
		if(e.getActionCommand().equals("Paste"))
		{
			paste();
		}
		if(e.getActionCommand().equals("Delete"))
		{
			delete();
		}
		if(e.getActionCommand().equals("Select All"))
		{
			selectAll();
		}
		if(e.getActionCommand().equals("Text Color"))
		{
			textColorChooser();
		}
		if(e.getActionCommand().equals("Background Color"))
		{
			backColorChooser();
		}
	}

	public void cut()
	{
		textArea.cut();
	}

	public void copy()
	{
		textArea.copy();
	}

	public void paste()
	{
		textArea.paste();
	}

	public void delete()
	{
		textArea.replaceSelection("");
	}

	public void selectAll()
	{
		textArea.requestFocusInWindow();
		textArea.selectAll();
	}

	public void textColorChooser()
	{
		Color backgroundColor = JColorChooser.showDialog(mainFrame,"Choose background color", Color.white);
        if(backgroundColor != null)
        {
            textArea.setForeground(backgroundColor);
        }
	}

	public void backColorChooser()
	{
		Color backgroundColor = JColorChooser.showDialog(mainFrame,"Choose background color", Color.white);
        if(backgroundColor != null)
        {
            textArea.setBackground(backgroundColor);
        }
	}

	public void compileFile()
	{
		String s="",resultErr="";
		int indexNumber = tabbedPane.getSelectedIndex();
		if(indexNumber != -1)
		{
			try
			{
				if(tabbedPane.getTitleAt(indexNumber).equals("untitled"))
				{
					saveAsFile();
				}
				else
				{
					String fname=tabbedPane.getTitleAt(indexNumber);
					if(!fname.matches("(.*).java"))
					{
						logArea.setText("Create a java file and then compile !\nThis editor has only java compiler!");
					}
					else
					{
						Process error=runtime.exec("C:\\Program Files\\Java\\jdk1.8.0_51\\bin\\javac.exe "+fname);
						BufferedReader err = new BufferedReader(new InputStreamReader(error.getErrorStream()));
						while(true)
						{
							String temp = err.readLine();
							if(temp!=null)
							{
								resultErr += temp;
								resultErr +="\n";
							}
							else
								break;
						}
						if(resultErr.equals("") || resultErr.trim().equals("Note: "+fname+" uses unchecked or unsafe operations.\nNote: Recompile with -Xlint:unchecked for details."))
						{
							if(!resultErr.trim().equals("Note: "+fname+" uses unchecked or unsafe operations.\nNote: Recompile with -Xlint:unchecked for details."))
							{
								logArea.setText("COMPILATION SUCCESSFUL  : "+ fname);
								err.close();						
							}
							else
							{
								logArea.setText("Note: "+fname+" uses unchecked or unsafe operations.\nNote: Recompile with -Xlint:unchecked for details.\nCOMPILATION SUCCESSFUL  : "+ fname);
								err.close();
							}
						}
						else
						{
							logArea.setText(resultErr);
						}
					}
				}
			}catch(Exception e){ System.out.println(e+" compile !");		}
		}
		else
		{
			logArea.setText("Create a file first !");
		}
	}

	public void runFile()
	{
		int start = 0;
		int indexNumber = tabbedPane.getSelectedIndex();
		try
		{
			if(indexNumber!=-1)
			{
				String fname=tabbedPane.getTitleAt(indexNumber);
				if(!fname.matches("(.*).java"))
				{
					logArea.setText("Create a java file and then run !\nThis editor has only java compiler!");
				}
				else
				{
					String resultErr="",resultOutput="";
					Process process =  runtime.exec("C:\\Program Files\\Java\\jdk1.8.0_51\\bin\\java.exe "+fname.replaceAll(".java",""));
					BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while(true)
					{
						String temp = output.readLine();
						if(temp!=null)
						{
							resultOutput += temp;
							resultOutput += "\n";
						}
						else
							break;
					}
					while(true)
					{
						String temp = error.readLine();
						if(temp!=null)
						{
							resultErr += temp;
							resultErr += "\n";
						}
						else
							break;
					}
					output.close();
					error.close();
					if(!resultOutput.equals("") && resultErr.equals(""))
						logArea.setText(resultOutput);
					if(resultOutput.equals(""))
						logArea.setText(resultErr);
					if(!resultOutput.equals("") && !resultErr.equals(""))
						logArea.setText(resultOutput + "\n" + resultErr);
				}
			}
			else
			{
				logArea.setText("Create a file first !");
			}
		}catch(Exception e){System.out.println(e+" run file.");}
	}

	public void saveFile()
	{
		try
		{
			String fileName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			if (fileName.equalsIgnoreCase("untitled"))
			{
				saveAsFile();
			}
			else
			{
				fileWriter = new FileWriter(fileName);
				print = new PrintWriter(fileWriter);
				print.println(textArea.getText());
				print.flush();
				fileWriter.close();
				logArea.setText("Saving : "+fileName);
				Thread.sleep(1000);
				logArea.setText("Saved : "+fileName);
			}
		}catch(Exception ex){	System.out.println(ex+"	 : save file ");	}
	}
	public void saveAsFile()
	{
		try
		{
			File file;
			String s=" ";
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int returnVal = fileChooser.showSaveDialog(null); 
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				file=fileChooser.getSelectedFile();
				fileWriter = new FileWriter(file.getName());
				fileWriter.write(textArea.getText());
				fileWriter.close();
				logArea.setText("Saving : "+file.getName());
				Thread.sleep(1000);
				logArea.setText("Saved : "+file.getName());
				tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),file.getName());
				//closeFile();
			}
			else
	        {
	            logArea.setText("Save command cancelled by user.");
	        }
		}catch(Exception ex){System.out.println(ex);ex.printStackTrace();}
	}
	public void openFile()
	{
		BufferedReader bufferedReader;
		File file;
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int returnVal = fileChooser.showOpenDialog(null);
		try
		{
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
         	    file = fileChooser.getSelectedFile();
            	bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            	//bufferedReader = new BufferedReader(new FileReader(file));

           		newFile();
            	textArea.read(bufferedReader,null);
                logArea.setText("Selected : "+file.getName());
			    tabbedPane.setTitleAt(count-1,file.getName());
			    tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
			}
	        else
	        {
	            logArea.setText("Open command cancelled by user.");
	        }
        }catch(Exception ex){System.out.println(ex+" : open file ");}
	}
	public void closeFile()
	{
		try
		{
			int indexNumber = tabbedPane.getSelectedIndex();
			if (indexNumber == -1)
			{
				logArea.setText("No tab selected.");
			}
			else
			{
				if(tabbedPane.getTitleAt(indexNumber).equals("untitled") && textArea.getText().equals(""))
				{
					tabbedPane.removeTabAt(indexNumber);
					count--;
					logArea.setText("");
				}
				else if(tabbedPane.getTitleAt(indexNumber).equals("untitled") && !textArea.getText().equals(""))
				{
					saveAsFile();
					tabbedPane.removeTabAt(indexNumber);
					count--;
					logArea.setText("");
				}
				else
				{
					tabbedPane.removeTabAt(indexNumber);
					count--;
					logArea.setText("");
				}
			}
		}catch(Exception ex){System.out.println(ex + "	: close file ");}
	}
	public void newFile()
	{
		try
		{
			JPanel p = new JPanel(new GridLayout(2,1,0,0));
			count++;
			textArea = new JTextArea();
			textScroll = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			p.add(textScroll);

			setArea(p);		

			tabbedPane.addTab("untitled",new ImageIcon("images/file.gif"),p);
			editPanel.add(tabbedPane);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		}catch(Exception ex){System.out.println(ex + " : new file ");}
	}
	public void setArea(JPanel p)
	{
		String[] languages = {"Plain Text","Java","C++","HTML"};
		JComboBox lang = new JComboBox(languages);
		lang.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        JComboBox combo = (JComboBox)e.getSource();
                        String currentLang = (String)combo.getSelectedItem();
                        if(currentLang.equals("Java"))
                        {
                        	textArea.setText("public class HelloWorld \n{\n\tpublic static void main(String... s)\n\t{\n\t\tSystem.out.println(\"Hello World\");\n\t}\n}");
                        }
                        if(currentLang.equals("C++"))
          				{
          					textArea.setText("#include <iostream.h>\n#include <conio.h> \n\n int main()\n{\n\tclrscr();\n\n\treturn 0;\n}");
          				}
          				if(currentLang.equals("HTML"))
          				{
          					textArea.setText("<!DOCTYPE html>\n<html>\n    <title></title>\n    <body></body>\n<\\html>");
          				}
                    }
                });
		p.add(lang);
	}
	public void setMenu()
	{
		try
		{
			//First menu - File : 
			menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			menuBar.add(menu);

				//First menu item in menu - File - New File : 
				menuItem = new JMenuItem("New File",KeyEvent.VK_N);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Second menu item in menu - File - Open File : 
				menuItem = new JMenuItem("Open",KeyEvent.VK_O);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Third menu item in menu - File - Save  : 
				menuItem = new JMenuItem("Save",KeyEvent.VK_S);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK);
				//	2 = ActionEvent.CTRL_MASK
				//  1 = ActionEvent.SHIFT_MASK
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Fourth menu item in menu - File - Save As : 
				menuItem = new JMenuItem("Save As",KeyEvent.VK_A);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Fifth menu item in menu - File - Close File :
				menu.addSeparator(); 
				menuItem = new JMenuItem("Close File",KeyEvent.VK_C);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Sixth menu item in menu - File - Exit :
				menu.addSeparator(); 
				menuItem = new JMenuItem("Exit",KeyEvent.VK_E);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

			//Second Menu - Edit :
			menu = new JMenu("Edit");
			menu.setMnemonic(KeyEvent.VK_E);
			menuBar.add(menu);

				//First menu item in menu - Edit - Cut : 
				menuItem = new JMenuItem("Cut",KeyEvent.VK_T);
				menuItem.addActionListener(this);
				menu.add(menuItem);

				//Second menu item in menu - Edit - Copy : 
				menuItem = new JMenuItem("Copy",KeyEvent.VK_C);
				menuItem.addActionListener(this);
				menu.add(menuItem);

				//Third menu item in menu - Edit - Paste : 
				menuItem = new JMenuItem("Paste",KeyEvent.VK_P);
				menuItem.addActionListener(this);
				menu.add(menuItem);

				//Fourth menu item in menu - Edit - Delete : 
				menuItem = new JMenuItem("Delete",KeyEvent.VK_D);
				menuItem.addActionListener(this);
				menu.add(menuItem);

				//Fifth menu item in menu - Edit - SelectAll : 
				menu.addSeparator();
				menuItem = new JMenuItem("Select All",KeyEvent.VK_A);
				menuItem.addActionListener(this);
				menu.add(menuItem);

			//Third Menu - Format :
			menu = new JMenu("Format");
			menu.setMnemonic(KeyEvent.VK_T);
			menuBar.add(menu);

				//First menu item in menu - Format - Setting text color : 
				menuItem = new JMenuItem("Text Color",KeyEvent.VK_T);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Second menu item in menu - Format - setting background color : 
				menuItem = new JMenuItem("Background Color",KeyEvent.VK_B);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_2,ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

			//Fourth Menu - Run :
			menu = new JMenu("Run");
			menu.setMnemonic(KeyEvent.VK_R);
			menuBar.add(menu);

				//First menu item in menu - Run - Compile : 
				menuItem = new JMenuItem("Compile",KeyEvent.VK_C);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_F5,ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);

				//Second menu item in menu - Run - Run : 
				menuItem = new JMenuItem("Run",KeyEvent.VK_R);
				menuItem.addActionListener(this);
				i=KeyStroke.getKeyStroke(KeyEvent.VK_F9,ActionEvent.CTRL_MASK);
				menuItem.setAccelerator(i);
				menu.add(menuItem);
		}catch(Exception ex){System.out.println(ex);ex.printStackTrace();}
	}
}