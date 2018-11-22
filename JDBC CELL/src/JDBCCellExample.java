
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.ResultSetMetaData;
	import java.sql.Statement;
	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.*;
	import java.io.FileWriter;
	import java.io.PrintWriter;

		class JDBCCellExample extends JFrame implements ActionListener {
			
			private JButton exportButton = new JButton("Export All Data");
			private JButton totalDroppedCallsButton = new JButton(" Export Total Number of Dropped Calls");
			private JButton numRecForCellButton = new JButton("Number of Records for Cell : ");
			private JButton recordsAfterButton = new JButton("List Records After :");
			private JTextField cellIDTF=new JTextField(12);
			private JTextField timeTF=new JTextField(12);
			private Connection con = null;
			private Statement stmt = null;
			

			public JDBCCellExample(String str)
			{  
				super(str);
				getContentPane().setLayout(new GridLayout(3,2));
				initDBConnection();
				getContentPane().add(exportButton);
				getContentPane().add(totalDroppedCallsButton);
				getContentPane().add(numRecForCellButton);
				getContentPane().add(cellIDTF);
				getContentPane().add(recordsAfterButton);
				getContentPane().add(timeTF);
				exportButton.addActionListener(this);
				totalDroppedCallsButton.addActionListener(this);
				numRecForCellButton.addActionListener(this);
				recordsAfterButton.addActionListener(this);		
				setSize(500,200);
				setVisible(true);  
			}
			
			private void initDBConnection(){
				try{
					Class.forName("com.mysql.jdbc.Driver");
					String url="jdbc:mysql://localhost:3306/PM";
				    con = DriverManager.getConnection(url, "root", "admin");
				    stmt = con.createStatement();
				}
				catch(Exception e){System.out.print("Failed to initialise DB Connection");
				}	
			}
		
			
			public void actionPerformed(ActionEvent e){
				Object target=e.getSource();
				ResultSet rs=null;
				String cmd = null;
				if (target.equals(exportButton)){  		
					cmd="select * from perf";			
				}
				else if(target.equals(totalDroppedCallsButton)){  		
					cmd = " Select count(*) from perf where Record_Description = 'Dropped Calls';";
				}
				else if(target.equals(numRecForCellButton)){ 
					String no = cellIDTF.getText();
					cmd = " Select cell_id, count(*) from perf where Cell_Id = '"+no+"';";
				}
				else if(target.equals(recordsAfterButton)){ 
					String time = timeTF.getText();
					cmd = "Select * from perf where Date > '"+time+"';";
				}
				
				
				try{					
					rs= stmt.executeQuery(cmd); 	
					writeToFile(rs);
				}
				catch(Exception e1){e1.printStackTrace();}
			}		

			public static void main(String args[]) {
				new JDBCCellExample("Cell Performance Data Export");
			}
			
			
			private void writeToFile(ResultSet rs){
				try{
					FileWriter outputFile = new FileWriter("CellOutput.csv");
					PrintWriter printWriter = new PrintWriter(outputFile);
					ResultSetMetaData rsmd = rs.getMetaData();
					int numColumns = rsmd.getColumnCount();
					
					for(int i=0;i<numColumns;i++){
						printWriter.print(rsmd.getColumnLabel(i+1)+",");
					}
					printWriter.print("\n");
					while(rs.next()){
						for(int i=0;i<numColumns;i++){
							printWriter.print(rs.getString(i+1)+",");
						}
						printWriter.print("\n");
						printWriter.flush();
					}
					printWriter.close();
				}
				catch(Exception e){e.printStackTrace();}
			

	} 
}
