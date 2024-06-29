import java.sql.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Main class for the Sports Management project
public class SportsManagement{
    public static void main(String[] aregisterPlayers) {
        try {
            // Establishing connection to the database
            String url = "jdbc:mysql://localhost:3306/sportsmanagement";
            String user = "root";
            String pass = "lakshman";
            Connection con = DriverManager.getConnection(url, user, pass);
            if (con != null) 
                System.out.println("Successfully Connected");
            
            Statement st = con.createStatement();

            // Creating the main frame for the application
            JFrame mainFrame = new JFrame("SPORTS MANAGEMENT");
            mainFrame.setBounds(150, 70, 400, 300);

            // Creating button to enter the sports section
            JButton enterIntoSports = new JButton("SPORTS");
            enterIntoSports.setBounds(150, 100, 90, 30);

            // Action listener for the button to enter into sports section
            enterIntoSports.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    // Frame for displaying sports
                    JFrame sportsFrame = new JFrame("SPORTS");
                    sportsFrame.setBounds(170, 100, 400, 300);

                    try {
                        // Retrieving list of tables from the database
                        ResultSet tables = st.executeQuery("show tables");
                        DefaultListModel<String> sportsHolder = new DefaultListModel<>();
                        while (tables.next()) {
                            sportsHolder.addElement(tables.getString(1));
                        }
                        
                        // Creating list for displaying sports
                        JList<String> sportsList = new JList<>(sportsHolder);
                        sportsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        sportsList.setFont(sportsList.getFont().deriveFont(22.0f));
                        
                        JScrollPane scrollPane = new JScrollPane(sportsList);
                        scrollPane.setBounds(20,20,100,100);

                        // Listener for selecting a sport
                        sportsList.addListSelectionListener(new ListSelectionListener() {
                            public void valueChanged(ListSelectionEvent e) {
                                if (!e.getValueIsAdjusting()) {
                                    
                                    // Frame for displaying details of selected sport
                                    String selectedTable = sportsList.getSelectedValue();
                                    JFrame sportDetails = new JFrame(selectedTable);
                                    sportDetails.setBounds(200,130,800,600);
                                    sportDetails.setLayout(null);
                                    try{
                                        // Querying database for selected sport details
                                        ResultSet sportQuery = st.executeQuery("SELECT * FROM "+selectedTable);
                                        ResultSetMetaData metaDataOfSport = sportQuery.getMetaData();
                                        int numColumns = metaDataOfSport.getColumnCount();
                                        ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
                                        while (sportQuery.next()) {
                                            ArrayList<String> tableData=new ArrayList<String>();
                                            for (int i = 1; i <= numColumns; i++) {
                                                tableData.add(sportQuery.getString(i));
                                            }
                                            data.add(tableData);
                                        }
                                        int numRows = data.size();
                                        int numCols = 4;

                                        // Creating data array for displaying in a table
                                        String[][] sportTableData = new String[numRows][numCols];

                                        for (int i = 0; i < numRows; i++) {
                                            ArrayList<String> innerList = data.get(i);
                                            for (int j = 0; j < numCols; j++) {
                                                sportTableData[i][j] = innerList.get(j);
                                            }
                                        }

                                        // Columns for the table
                                        String column[] = {"ID", "NAME", "AGE","SCORE"};

                                        // Creating table to display sport details
                                        JTable tableOfTheSport = new JTable(sportTableData, column) ;

                                        // Scroll pane for the table
                                        JScrollPane scrollForTable = new JScrollPane(tableOfTheSport);
                                        scrollForTable.setBounds(18, 20, 750,300);
                                        scrollForTable.setViewportView(tableOfTheSport);

                                        // Button for updating sport details
                                        JButton updatePlayer= new JButton("Update");
                                        updatePlayer.setBounds(315,450,150,50);
                                        updatePlayer.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e){

                                                // Frame for updating sport details
                                                JFrame updateFrame=new JFrame("Update");
                                                updateFrame.setBounds(1000,100,390,390);
                                                updateFrame.setLayout(null);

                                                JLabel idString=new JLabel();
                                                idString.setBounds(50,0,200,100);
                                                idString.setText("Select id :");
                                                idString.setVisible(true);

                                                ArrayList<String> idlist = new ArrayList<String>();
                                                try{
                                                    // Retrieving list of IDs for the selected sport
                                                    ResultSet ids = st.executeQuery("select id from " + selectedTable);
                                                    idlist.add("Click to Select");
                                                    while (ids.next()){
                                                        idlist.add(ids.getString(1));
                                                    }
                                                }
                                                catch(SQLException el){
                                                    System.out.println(el);
                                                }

                                                Object[] ArrayOfIds = idlist.toArray();
                                                JComboBox ListOfIds = new JComboBox(ArrayOfIds);
                                                ListOfIds.setBounds(150, 35, 200, 30);
                                                ListOfIds.setVisible(true);

                                                JLabel fieldString = new JLabel();
                                                fieldString.setBounds(50,65,200,100);
                                                fieldString.setText("Select Field :");

                                                String[] fields = { "Click to Select","Name","Age","score"}; 
                                                JComboBox<String> fieldsList = new JComboBox<String>(fields);
                                                fieldsList.setBounds(150,97,200,30);
                                                fieldsList.setVisible(true);

                                                JLabel newString= new JLabel();
                                                newString.setBounds(50,130,200,100);
                                                newString.setText("New value :");

                                                JTextField updateValue=new JTextField();
                                                updateValue.setBounds(150, 170, 200, 30);

                                                JButton updateButton=new JButton("Update");
                                                updateButton.setBounds(50, 250, 100, 50);

                                                // Action listener for updating sport details
                                                updateButton.addActionListener(new ActionListener(){
                                                    public void actionPerformed(ActionEvent e){
                                                        try{
                                                            if(fieldsList.getSelectedItem()=="Click to Select" || ListOfIds.getSelectedItem()=="Click to Select"){
                                                                throw new ArithmeticException();
                                                            }
                                                            if(fieldsList.getSelectedItem()=="Name"){
                                                            st.executeUpdate("Update "+selectedTable+" set "+fieldsList.getSelectedItem()+" = '"+updateValue.getText()+"' where id='"+ListOfIds.getSelectedItem()+"'");
                                                            }
                                                            else{
                                                            st.executeUpdate("Update "+selectedTable+" set "+fieldsList.getSelectedItem()+" = "+updateValue.getText()+" where id='"+ListOfIds.getSelectedItem()+"'");
                                                            }
                                                            JOptionPane.showMessageDialog(updateFrame,"Successfully Updated");  
                                                            updateFrame.setVisible(false);   
                                                            sportDetails.setVisible(false);   
                                                        }
                                                        catch(SQLException SyntaxError){
                                                            JOptionPane.showMessageDialog(updateFrame, "Enter correct details","OOPS!!",JOptionPane.ERROR_MESSAGE);
                                                        }
                                                        catch(ArithmeticException em){
                                                            JOptionPane.showMessageDialog(updateFrame, "Select the feilds","OOPS!!",JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }
                                                });

                                                JButton cancelButton=new JButton("Cancel");
                                                cancelButton.setBounds(200, 250, 100, 50);
                                                cancelButton.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e){
                                                            updateFrame.setVisible(false);
                                                        }
                                                });
                                                
                                            updateFrame.setVisible(true);
                                            updateFrame.add(idString);
                                            updateFrame.add(ListOfIds);
                                            updateFrame.add(fieldString);
                                            updateFrame.add(fieldsList);
                                            updateFrame.add(newString);
                                            updateFrame.add(updateValue);
                                            updateFrame.add(updateButton);
                                            updateFrame.add(cancelButton);
                                            }
                                        });
            
                                        // Button for registering new sport
                                        JButton registerPlayer=new JButton("Register");
                                        registerPlayer.setBounds(565,450,150,50);

                                        // Action listener for registering new sport
                                        registerPlayer.addActionListener(new ActionListener(){
                                            public void actionPerformed(ActionEvent e){

                                                // Frame for registering new sport
                                                JFrame registerFrame = new JFrame("Register");
                                                registerFrame.setBounds(1000,100,500,400);
                                                registerFrame.setLayout(null);

                                                JLabel idString=new JLabel("Enter the id : ");
                                                idString.setBounds(60,20,100,40);

                                                JTextField idRegister=new JTextField();
                                                idRegister.setBounds(220,20,200,40);

                                                JLabel nameString=new JLabel("Enter the Name : ");
                                                nameString.setBounds(60,70,100,40);

                                                JTextField nameRegister=new JTextField();
                                                nameRegister.setBounds(220,70,200,40);

                                                JLabel ageString=new JLabel("Enter the Age : ");
                                                ageString.setBounds(60,120,100,40);

                                                JTextField ageRegister=new JTextField();
                                                ageRegister.setBounds(220,120,200,40);

                                                JLabel scoreString=new JLabel("Enter the score  :");
                                                scoreString.setBounds(60,170,120,40);

                                                JTextField scoreRegister=new JTextField();
                                                scoreRegister.setBounds(220,170,200,40);

                                                JButton registerButton=new JButton("Register");
                                                registerButton.setBounds(60, 250, 150, 50);

                                                JButton cancelButton=new JButton("Cancel");
                                                cancelButton.setBounds(260, 250, 150, 50);
                                                cancelButton.addActionListener(new ActionListener(){
                                                    public void actionPerformed(ActionEvent e){
                                                        registerFrame.setVisible(false);
                                                    }
                                                });

                                                // Action listener for registering new sport
                                                registerButton.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e){
                                                        try{
                                                            String id=idRegister.getText();
                                                            String age=ageRegister.getText();
                                                            String score=scoreRegister.getText();
                                                            if(!(id.matches("\\d+")) || !(score.matches("\\d+")) || !(age.matches("\\d+"))){
                                                                throw new ArithmeticException();
                                                            }
                                                            st.executeUpdate("insert into "+selectedTable+ " values('"+idRegister.getText()+"','"+nameRegister.getText()+"',"+ageRegister.getText()+","+scoreRegister.getText()+")");

                                                            JOptionPane.showMessageDialog(registerFrame,"Successfully Registered");
                                                            registerFrame.setVisible(false);
                                                            sportDetails.setVisible(false);
                                                        }

                                                        catch(SQLException Duplicate){
                                                            JOptionPane.showMessageDialog(registerFrame, "Duplicate id not Allowed","OOPS!!",JOptionPane.ERROR_MESSAGE);
                                                        }
                                                        catch(ArithmeticException giveInt){
                                                            JOptionPane.showMessageDialog(registerFrame, "Enter Correct details", "OOPS!!", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }

                                                });
                                
                                                registerFrame.add(idString);
                                                registerFrame.add(idRegister);
                                                registerFrame.add(nameString);
                                                registerFrame.add(nameRegister);
                                                registerFrame.add(ageString);
                                                registerFrame.add(ageRegister);
                                                registerFrame.add(scoreString);
                                                registerFrame.add(scoreRegister);
                                                registerFrame.add(registerButton);
                                                registerFrame.add(cancelButton);    
                                                registerFrame.setVisible(true);
                                                }
                                                
                                        });

                                        // Button for deleting a sport
                                        JButton deletePlayer=new JButton("Delete");
                                        deletePlayer.setBounds(65,450,150,50);

                                        // Action listener for deleting a sport
                                        deletePlayer.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e){
                                                // Frame for deleting a sport
                                                JFrame deleteFrame=new JFrame("Delete");
                                                deleteFrame.setBounds(1000,100,300,250);
                                                deleteFrame.setLayout(null);

                                                JLabel idString=new JLabel("Select The ID : ");
                                                idString.setBounds(30,40,100,25);

                                                try{
                                                    ResultSet ids=st.executeQuery("select id from "+selectedTable); 
                                                    ArrayList<String>idList = new ArrayList<String>();
                                                    idList.add("--SELECT id--");
                                                    while(ids.next()){
                                                        idList.add(ids.getString(1));
                                                    }
                                                
                                                Object[] idArray=idList.toArray();
                                                JComboBox idDropDown=new JComboBox(idArray);
                                                idDropDown.setBounds(120,40,120,25);

                                                JButton deleteButton=new JButton("Delete");
                                                deleteButton.setBounds(30, 130, 100, 30);

                                                JButton cancelButton=new JButton("Cancel");
                                                cancelButton.setBounds(150,130,100,30);

                                                cancelButton.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e){
                                                        deleteFrame.setVisible(false);
                                                    }
                                                });
                                                
                                                idDropDown.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e){
                                                            deleteButton.addActionListener(new ActionListener() {
                                                                public void actionPerformed(ActionEvent e){
                                                                    try{
                                                                        if(idDropDown.getSelectedItem()=="--SELECT id--"){
                                                                            throw new ArithmeticException();
                                                                        }
                                                                        st.executeUpdate("delete from cricket where id='"+idDropDown.getSelectedItem()+"'");
                                                                        JOptionPane.showMessageDialog(deleteFrame,"Successfully Deleted");
                                                                        deleteFrame.setVisible(false);
                                                                        sportDetails.setVisible(false);
                                                                    }
                                                                    catch(ArithmeticException giveInt){
                                                                        JOptionPane.showMessageDialog(deleteFrame, "Select the ID","OOPS!!",JOptionPane.ERROR_MESSAGE);
                                                                    }
                                                                    catch(SQLException eml){
                                                                        System.out.println(eml);
                                                                    }
                                                                }
                                                            });
                                                    }
                                                });
                                                deleteFrame.add(cancelButton);
                                                deleteFrame.add(deleteButton);
                                                deleteFrame.add(idDropDown);
                                                deleteFrame.add(idString);
                                                deleteFrame.setVisible(true);
                                            }
                                            catch(Exception emm){
                                                System.out.println(emm);
                                            }
                                        }
                                    }); 
                                    sportDetails.add(scrollForTable);  
                                    sportDetails.add(updatePlayer);
                                    sportDetails.add(registerPlayer);
                                    sportDetails.add(deletePlayer);
                                    sportDetails.setSize(800, 600);    
                                    sportDetails.setVisible(true);
                                    }
                                    catch(Exception em){}
                                    sportsList.clearSelection();
                                }
                            }
                        });
                        
                        sportsFrame.add(scrollPane);
                        sportsFrame.setVisible(true);
                    } 
                    catch (Exception ex) {}
                }
            });

            mainFrame.setLayout(null);
            mainFrame.add(enterIntoSports);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
