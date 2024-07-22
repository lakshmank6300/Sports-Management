import java.sql.*;
import java.util.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.AccessException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Main class for the Sports Management project
public class InternshipProject{
    public static void main(String[] args) {
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
            mainFrame.setBounds(500, 150, 400, 300);
            
            // Creating button to enter the sports section
            JButton enterIntoSports = new JButton("SPORTS");
            enterIntoSports.setBounds(130,60,150,30);

            //Creating button to enter to Check Players
            JButton CheckPlayers = new JButton("CHECK PLAYERS");
            CheckPlayers.setBounds(130, 100, 150, 30);

            //declaring the data ArrayList which holds the data to be printed
            ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();

            // Action listener for the button to enter into sports section
            enterIntoSports.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JFrame frame = new JFrame("Sports Management");
                    frame.setBounds(150,20,1200, 770);
                    frame.setResizable(false);
                    frame.setLayout(null);

                    //JTextArea is used to display the text from the database
                    JTextArea area = new JTextArea();
                    area.setBounds(100, 130, 1000, 500);
                    area.setFont(new Font("Calibri", Font.PLAIN, 20));
                    area.setEditable(false);
                    frame.add(area);

                    //JLabel->allows you to create labels to display some text
                    JLabel label = new JLabel("Select Sport:");
                    label.setFont(new Font("Calibri", Font.BOLD, 25));
                    label.setBounds(320, 20, 160, 30);
                    label.setBackground(Color.white);
                    frame.add(label);
                    
                    //Taking names of tables from database to view in dropdown button to select
                    ArrayList<String> listoftables = new ArrayList<String>();
                    listoftables.add("SELECT SPORT");
                        try{
                            ResultSet tables=st.executeQuery("show tables");
                            while(tables.next()){
                                listoftables.add(tables.getString(1));
                            }
                        }catch(SQLException ems){}
                    
                    //creating dropdown for selecting the sport
                    Object[] ArrayOftables = listoftables.toArray();
                    JComboBox ListOfTables = new JComboBox(ArrayOftables);
                    ListOfTables.setBounds(500, 15, 200, 40);
                    ListOfTables.setVisible(true);
                    frame.add(ListOfTables);
                    
                    //creating viewPlayers button using Jbutton
                    JButton ViewPlayers = new JButton("View Players");
                    ViewPlayers.setFont(new Font("Calibri", Font.BOLD, 20));
                    ViewPlayers.setBounds(710, 15, 205, 40);
                    frame.add(ViewPlayers);
                    

                    ViewPlayers.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            data.clear();
                            Object selectedTable = ListOfTables.getSelectedItem();
                            try{
                                if(selectedTable=="SELECT SPORT"){
                                    throw new Exception();
                                }
                            try{
                                // Querying database for selected sport details
                                area.setVisible(false);
                                ResultSet sportQuery = st.executeQuery("SELECT * FROM "+selectedTable);
                                ResultSetMetaData metaDataOfSport = sportQuery.getMetaData();
                                int numColumns = metaDataOfSport.getColumnCount();
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
                                JTable tableOfTheSport = new JTable(sportTableData, column);

                                // Scroll pane for the table
                                JScrollPane scrollForTable = new JScrollPane(tableOfTheSport);
                                scrollForTable.setBounds(100, 130, 1000, 500);
                                tableOfTheSport.setEnabled(false);
                                scrollForTable.setViewportView(tableOfTheSport);
                                frame.add(scrollForTable);

                        }catch(SQLException ems){}
                        }catch(Exception exs){
                            JOptionPane.showMessageDialog(area, "Select Specific Sport", "OOPS!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    });
                    
                    
                    //creating a view all Sports using JButton
                    JButton sports = new JButton("View All Sports");
                    sports.setFont(new Font("Calibri", Font.BOLD, 20));
                    sports.setBounds(120, 70, 205, 40);
                    frame.add(sports);

                    sports.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            area.setVisible(true);
                            data.clear();
                            area.setText("");
                            for(int i=1;i<listoftables.size();i++){ 
                                ArrayList<String> temp=new ArrayList<>();
                                area.append(listoftables.get(i)+"\n");
                                temp.add(listoftables.get(i));
                                data.add(temp);
                               }
                        }
                    });

                    //creating a button3 using JButton
                    JButton addPlayer = new JButton("Add Player");
                    addPlayer.setFont(new Font("Calibri", Font.BOLD, 20));
                    addPlayer.setBounds(370, 70, 205, 40);
                    frame.add(addPlayer);

                    addPlayer.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            
                            Object selectedTable = ListOfTables.getSelectedItem();
                            try{
                                if(selectedTable=="SELECT SPORT"){
                                    throw new Exception();
                                }
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
                                        // sportDetails.setVisible(false);
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
                        catch(Exception exs){
                            JOptionPane.showMessageDialog(area, "Select Specific Sport", "OOPS!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    });


                    //creating a UpdatePlayer button using JButton
                    JButton UpdatePlayer = new JButton("Update Player");
                    UpdatePlayer.setFont(new Font("Calibri", Font.BOLD, 20));
                    UpdatePlayer.setBounds(620, 70, 205, 40);
                    frame.add(UpdatePlayer);

                    UpdatePlayer.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            Object selectedTable = ListOfTables.getSelectedItem();
                            try{
                                if(selectedTable=="SELECT SPORT"){
                                    throw new Exception();
                                }
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
                                        // sportDetails.setVisible(false);   
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
                        catch(Exception exs){
                            JOptionPane.showMessageDialog(area, "Select Specific Sport", "OOPS!!", JOptionPane.ERROR_MESSAGE);
                        }
                        }
                    });
                    

                    //creating a deletePlayer buton using JButton
                    JButton deletePlayer = new JButton("Delete Player");
                    deletePlayer.setFont(new Font("Calibri", Font.BOLD, 20));
                    deletePlayer.setBounds(870, 70, 205, 40);
                    frame.add(deletePlayer);

                    deletePlayer.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            // Frame for deleting a sport
                            Object selectedTable = ListOfTables.getSelectedItem();
                            
                            try{
                                if(selectedTable=="SELECT SPORT"){
                                    throw new Exception();
                                }

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
                                                    st.executeUpdate("delete from "+selectedTable+" where id='"+idDropDown.getSelectedItem()+"'");
                                                    JOptionPane.showMessageDialog(deleteFrame,"Successfully Deleted");
                                                    deleteFrame.setVisible(false);
                                                    // sportDetails.setVisible(false);
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
                        catch(SQLException emm){}
                    }
                    catch(Exception exs){
                        JOptionPane.showMessageDialog(area, "Select Specific Sport", "OOPS!!", JOptionPane.ERROR_MESSAGE);
                    }
                    }
                }); 

                //creating Print Button using Jbutton
                JButton Print=new JButton("Print CSV File");
                Print.setFont(new Font("Calibri", Font.BOLD, 20));
                Print.setBounds(870, 660, 205, 40);
                frame.add(Print);

                Print.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        
                        String FileName = JOptionPane.showInputDialog(null, "Enter file name without Extension", "CSV FILE", 3);
                        FileName+=".csv";
                        try (FileWriter csvWriter = new FileWriter(FileName)) {
                            for (ArrayList<String> rowData : data) {
                                csvWriter.append(String.join(",", rowData));
                                csvWriter.append("\n");
                            }
                            area.setText("");
                            JOptionPane.showMessageDialog(null, "File Created Successfully", "Success", 1);
                        } catch (IOException ess) {
                            JOptionPane.showMessageDialog(null, "Enter Name","OOPS!!",JOptionPane.ERROR_MESSAGE);
                        }
                        catch(NullPointerException np){}
                    }
                });

                frame.setVisible(true);
                }
            });

            CheckPlayers.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JFrame sportsFrame = new JFrame("SPORTS");
                    sportsFrame.setBounds(350, 100, 1000, 700);
                    sportsFrame.setLayout(null);
                    
                    JLabel enter=new JLabel("Select Sports");
                    enter.setBounds(40,20,150,30);
                    enter.setFont(new Font("Calibri",Font.BOLD,20));
                    sportsFrame.add(enter);

                    JLabel player=new JLabel("PLAYERS");
                    player.setBounds(300,20,150,30);
                    player.setFont(new Font("Calibri",Font.BOLD,30));
                    sportsFrame.add(player);
                    int c=50;

                    JButton Print=new JButton("Print CSV File");
                    Print.setFont(new Font("Calibri", Font.BOLD, 20));
                    Print.setBounds(660, 500, 205, 40);
                    sportsFrame.add(Print);

                    JTextArea players=new JTextArea();
                    players.setBounds(200,50,700,400);
                    players.setFont(new Font("Calibri",Font.BOLD,20));
                    players.setEditable(false);

                    Print.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            
                            String FileName = JOptionPane.showInputDialog(null, "Enter file name without Extension", "CSV FILE", 3);
                            FileName+=".csv";
                            try (FileWriter csvWriter = new FileWriter(FileName)) {
                                for (ArrayList<String> rowData : data) {
                                    csvWriter.append(String.join(",", rowData));
                                    csvWriter.append("\n");
                                }
                                if(FileName.length()==0)
                                    throw new IOException();
                                players.setText("");
                                JOptionPane.showMessageDialog(null, "File Created Successfully", "Success", 1);
                            } catch (IOException ess) {
                                JOptionPane.showMessageDialog(null, "Enter Name","OOPS!!",JOptionPane.ERROR_MESSAGE);
                            }
                            catch(NullPointerException np){}
                        }
                    });

                    JScrollPane scrollForPlayers = new JScrollPane(players);
                    scrollForPlayers.setBounds(200, 50, 700,400);
                    scrollForPlayers.setViewportView(players);
                    sportsFrame.add(scrollForPlayers);

                    List<String> selectedCheckboxNames = new ArrayList<>();
                    try {
                        // Retrieving list of tables from the database
                        ResultSet tables = st.executeQuery("show tables");

                        while (tables.next()) {
                            JCheckBox checkbox = new JCheckBox(tables.getString(1));
                            checkbox.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    JCheckBox clickedCheckbox = (JCheckBox) e.getSource();
                                    
                                    String clickedCheckboxName = clickedCheckbox.getText();
                                    if (clickedCheckbox.isSelected()) {
                                        selectedCheckboxNames.add(clickedCheckboxName);
                                    } else {
                                        selectedCheckboxNames.remove(clickedCheckboxName);
                                    }
                                    players.setText("");
                                    String q= "select id,name from ";
                                    String query="";
                                    try{
                                            int i=0,n=selectedCheckboxNames.size();
                                            while(i<n-1){
                                                query+=q;
                                                query+=selectedCheckboxNames.get(i);
                                                query=query.concat(" union ");
                                                i++;
                                            }
                                            ResultSet playersset = st.executeQuery(query+" "+q+selectedCheckboxNames.get(i)+";");
                                            
                                            data.clear();
                                            while(playersset.next()){
                                                ArrayList<String>playersNames=new ArrayList<>();
                                                playersNames.add(playersset.getString(2));
                                                data.add(playersNames);
                                                players.append(playersset.getString(2)+"\n");
                                            }
                                    }catch(SQLException w){System.out.println("Exception");}
                                }
                            });
                            checkbox.setBounds(30,20+c,150,30);
                            checkbox.setFont(new Font("Calibri",Font.BOLD,20));
                            sportsFrame.add(checkbox);
                            c+=40;
                        }
                        sportsFrame.add(Print);
                        sportsFrame.setVisible(true);
                    } catch (SQLException em) {
                    }
                }
            });
            mainFrame.setLayout(null);
            mainFrame.add(CheckPlayers);
            mainFrame.add(enterIntoSports);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
