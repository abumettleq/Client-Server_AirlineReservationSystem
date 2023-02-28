import java.io.*;
import java.net.*;
import java.sql.*;

import com.AES.AESUtils;

class ClientHandler implements Runnable {

        private final Socket clientSocket;
        final String secretKey = "JHKLXABYZC!!!!";

    // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
            System.out.println("Client connected: " + this.clientSocket.getInetAddress().getHostAddress());
        }   

        public void run()
        {
            String username = "system";
            String password = "root1234";
            String url = "jdbc:oracle:thin:@localhost:1521/oraclpdb";
            
            PrintWriter toClient = null;
            BufferedReader fromClient = null;

            try {

                // set the output stream to a client
                toClient = new PrintWriter(clientSocket.getOutputStream(), true);

                // get the input stream from a client
                fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String query; // Query sent from client
                while ((query = AESUtils.decrypt(fromClient.readLine(),secretKey)) != null) {
                    // writing the received message from client

                    System.out.printf("New query received: %s\n", query);
                    try
                    {
                        Class.forName("oracle.jdbc.driver.OracleDriver");

                        Connection connection = DriverManager.getConnection(url, username, password);
                        Statement statement = connection.createStatement();

                        if (query.startsWith("signup=")) // user requested to create a new account
                        {
                            String this_email = query.substring(7);
                            ResultSet result = statement.executeQuery("select email from account where email = '"+this_email+"'");
                            if (!result.isBeforeFirst()) {
                                toClient.println(AESUtils.encrypt("Continue",secretKey));
                                statement.executeQuery(AESUtils.decrypt(fromClient.readLine(),secretKey));
                                statement.executeQuery("Update Account SET CreatedOn = SYSDATE, isActive = 0 WHERE Email = '"+this_email+"'");
                                System.out.println("New account created with email = "+this_email);
                            }
                            else
                            {
                                System.out.println(this_email+" is trying to register again!");
                                toClient.println(AESUtils.encrypt("Email already used.",secretKey));
                            }
                            result.close();
                        }
                        else if(query.startsWith("login="))
                        {
                            String this_email = query.substring(6);
                            System.out.println(this_email + " is trying to log in ...");

                            ResultSet result = statement.executeQuery("select Email, Password from account where Email = '"+ this_email +"'");

                            if (!result.isBeforeFirst() ) {
                                System.out.println(this_email + " failed to login! (Couldn't find Email.)");
                                toClient.println(AESUtils.encrypt("email_404",secretKey));
                            }
                            else
                            {
                                result.next();
                                if(result.getString(1).equals(this_email))
                                {
                                    toClient.println(AESUtils.encrypt("Continue",secretKey));
                                    String this_password = AESUtils.decrypt(fromClient.readLine(),secretKey);
                                    if(result.getString(2).equals(this_password))
                                    {
                                        toClient.println(AESUtils.encrypt("Login Success.",secretKey));
                                        statement.executeQuery("Update Account SET LastLogin = SYSDATE, isActive = 1 WHERE Email = '" + this_email+ "'");
                                        System.out.println(this_email + " logged in.");

                                        result = statement.executeQuery("Select FName,MName,LName,DOB,MOBILE_NO,CREATEDON from Account where Email = '"+ this_email+"'");
                                        result.next();
                                        String this_name = result.getString(1) +" "+ result.getString(2) +" "+ result.getString(3);
                                        String this_dob = result.getString(4).substring(0,10);
                                        String this_mobileno = result.getString(5);
                                        String this_createdon = result.getString(6);

                                        toClient.println(AESUtils.encrypt(this_name,secretKey));
                                        toClient.println(AESUtils.encrypt(this_dob,secretKey));
                                        toClient.println(AESUtils.encrypt(this_mobileno,secretKey));
                                        toClient.println(AESUtils.encrypt(this_createdon,secretKey));
                                    }
                                    else
                                    {
                                        System.out.println(this_email + " entered wrong password.");
                                        toClient.println(AESUtils.encrypt("Wrong Password.",secretKey));
                                    }
                                }
                                else
                                {
                                    System.out.println("Client is sending incomplete sql commands!");
                                    toClient.println(AESUtils.encrypt("Couldn't find Email.",secretKey));
                                }
                            }
                            result.close();
                        }
                        else if(query.startsWith("ticket_from="))
                        {
                            String from = query.substring(12);
                            String to = AESUtils.decrypt(fromClient.readLine(),secretKey);
                            //System.out.println("Server executed: Select * from Ticket where fromwhere = '"+from+"' AND towhere = '"+to+"' AND isBought = 0");
                            ResultSet result = statement.executeQuery("Select * from Ticket where fromwhere = '"+from+"' AND towhere = '"+to+"' AND isBought = 0");
                            if(!result.isBeforeFirst())
                            {
                                System.out.println("Database: No Tickets found!");
                                toClient.println(AESUtils.encrypt("Currently, No tickets available.",secretKey));
                            }
                            else
                            {
                                toClient.println(AESUtils.encrypt("receive_this",secretKey));
                                while (result.next())
                                {
                                    toClient.println(AESUtils.encrypt("start_now",secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(1),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(2),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(3),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(4),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(5),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(6),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(7),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(8),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(9),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(10),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(11),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(12),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(13),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(14),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(15),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(16),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(17),secretKey));
                                    toClient.println(AESUtils.encrypt(result.getString(18),secretKey));
                                }
                                toClient.println(AESUtils.encrypt("search_ends_here",secretKey));
                            }
                            result.close();
                        }
                        else if(query.startsWith("reserve="))
                        {
                            String this_ticket = query.substring(11);
                            String this_email = AESUtils.decrypt(fromClient.readLine(),secretKey);
                            ResultSet result = statement.executeQuery("Select Account_ID from Account where Email = '"+this_email+"'");
                            result.next();
                            String acc_id = result.getString(1);
                            statement.executeQuery("Insert into Inventory(Inventory_ID,Account_ID,Ticket_ID) VALUES ("+acc_id+","+acc_id+","+this_ticket+")");
                            statement.executeQuery("Update Ticket SET isBought = 1 Where Ticket_ID = "+this_ticket);
                            toClient.println(AESUtils.encrypt("Ticket has been reserved successfully!",secretKey));
                            System.out.println(this_email + " has reserved a ticket with ID = "+ this_ticket);
                            result.close();
                        }
                        else if(query.startsWith("show_inv="))
                        {
                            String this_email = query.substring(9);

                            ResultSet result = statement.executeQuery("Select Account_ID from Account where Email = '"+this_email+"'");
                            result.next();
                            int acc_id = result.getInt(1);
                            result.close();

                            ResultSet result1 = statement.executeQuery("select count(Ticket_ID) from Inventory where Account_ID = "+acc_id);
                            result1.next();
                            int number = result1.getInt(1);
                            result.close();
                            if(number <= 0)
                            {
                                toClient.println(AESUtils.encrypt("You don't own any ticket.",secretKey));
                            }
                            else {
                                int[] tickets_id = new int[number];
                                String[] toExecute = new String[tickets_id.length];

                                ResultSet prefinal_result = statement.executeQuery("select Ticket_ID from Inventory where Account_ID = " + acc_id);
                                for (int i = 0; i < tickets_id.length; i++) {
                                    prefinal_result.next();
                                    tickets_id[i] = prefinal_result.getInt(1);
                                    toExecute[i] = "Select * from Ticket where Ticket_ID = " + tickets_id[i];
                                }
                                prefinal_result.close();

                                toClient.println(AESUtils.encrypt("receive_this",secretKey));

                                ResultSet final_result;
                                for (String s : toExecute) {
                                    final_result = statement.executeQuery(s);
                                    final_result.next();
                                    toClient.println(AESUtils.encrypt("starts_now",secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(1),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(2),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(3),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(4),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(5),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(6),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(7),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(8),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(9),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(10),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(11),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(12),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(13),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(14),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(15),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(16),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(17),secretKey));
                                    toClient.println(AESUtils.encrypt(final_result.getString(18),secretKey));
                                    final_result.close();
                                }
                                toClient.println(AESUtils.encrypt("search_ends_here",secretKey));
                            }
                        }
                        else if(query.startsWith("cancel_ticket="))
                        {
                            String this_email = AESUtils.decrypt(fromClient.readLine(),secretKey);
                            ResultSet result = statement.executeQuery("Select Account_ID from Account where Email = '"+this_email+"'");
                            result.next();
                            String acc_id = result.getString(1);
                            String this_ticket = query.substring(14);
                            int rows = statement.executeUpdate("Delete from Inventory where Ticket_ID = "+this_ticket+" AND Account_ID = "+acc_id);
                            //System.out.println("Server executed: Delete from Inventory where Ticket_ID = "+this_ticket+" AND Account_ID = "+acc_id+"\nNumber of rows updated: "+rows);
                            if(rows != 0)
                            {
                                System.out.println("Server executed: Update Ticket SET isBought = 0 Where Ticket_ID = "+this_ticket);
                                statement.executeQuery("Update Ticket SET isBought = 0 Where Ticket_ID = "+this_ticket);
                                toClient.println(AESUtils.encrypt("Ticket cancelled successfully!",secretKey));
                            }
                            else
                            {
                                toClient.println(AESUtils.encrypt("You don't own a ticket with this ID!",secretKey));
                            }
                            result.close();
                        }
                        else if(query.startsWith("update_info="))
                        {
                            String this_email = query.substring(12);
                            String newQuery = AESUtils.decrypt(fromClient.readLine(),secretKey);
                            System.out.println("New subquery received: "+newQuery);
                            assert newQuery != null;
                            if(newQuery.startsWith("first_name="))
                            {
                                String FNAME = newQuery.substring(11);
                                statement.executeQuery("Update Account SET FNAME = '"+FNAME+"' where Email = '"+this_email+"'");
                                //toClient.println("First name has been updated!");
                            }
                            else if(newQuery.startsWith("middle_name="))
                            {
                                String MNAME = newQuery.substring(12);
                                statement.executeQuery("Update Account SET MNAME = '"+MNAME+"' where Email = '"+this_email+"'");
                                //toClient.println("Middle name has been updated!");
                            }
                            else if(newQuery.startsWith("last_name="))
                            {
                                String LNAME = newQuery.substring(10);
                                statement.executeQuery("Update Account SET LNAME = '"+LNAME+"' where Email = '"+this_email+"'");
                                toClient.println(AESUtils.encrypt("updated",secretKey));
                            }
                            else if(newQuery.startsWith("mobile_no="))
                            {
                                String MOBILE = newQuery.substring(10);
                                statement.executeQuery("Update Account SET Mobile_No = '"+MOBILE+"' where Email = '"+this_email+"'");
                                toClient.println(AESUtils.encrypt("Mobile number has been updated!",secretKey));
                            }
                            else if(newQuery.startsWith("birthdate="))
                            {
                                String DOB = newQuery.substring(10);
                                statement.executeQuery("Update Account SET DOB = TO_DATE('"+DOB+"' , 'YYYY-MM-DD') where Email = '"+this_email+"'");
                                toClient.println(AESUtils.encrypt("updated",secretKey));
                            }
                            else if(newQuery.startsWith("password="))
                            {
                                String this_password = newQuery.substring(9);
                                ResultSet result = statement.executeQuery("select Email from account where Email = '"+ this_email +"' AND Password = '"+ this_password +"'");
                                if(!result.isBeforeFirst())
                                {
                                    toClient.println(AESUtils.encrypt("wrong_password",secretKey));
                                }
                                else
                                {
                                    toClient.println(AESUtils.encrypt("Continue",secretKey));
                                    statement.executeQuery("Update Account SET Password = '"+AESUtils.decrypt(fromClient.readLine(),secretKey)+"' WHERE Email = '"+this_email+"'");
                                }
                                result.close();
                            }
                            System.out.println(this_email+" updated his info!");
                        }
                        else if(query.startsWith("get_info="))
                        {
                            String this_email = query.substring(9);
                            ResultSet result = statement.executeQuery("Select FName,MName,LName,DOB,MOBILE_NO,CREATEDON,Password from Account where Email = '"+ this_email+"'");
                            result.next();
                            String this_name = result.getString(1) +" "+ result.getString(2) +" "+ result.getString(3);
                            String this_dob = result.getString(4).substring(0,10);
                            String this_mobileno = result.getString(5);
                            String this_createdon = result.getString(6);
                            String this_password = result.getString(7);

                            toClient.println(AESUtils.encrypt(this_name,secretKey));
                            toClient.println(AESUtils.encrypt(this_dob,secretKey));
                            toClient.println(AESUtils.encrypt(this_mobileno,secretKey));
                            toClient.println(AESUtils.encrypt(this_createdon,secretKey));
                            toClient.println(AESUtils.encrypt(this_password,secretKey));
                        }
                        else
                        {
                            System.out.println("Client is sending incomplete commands!");
                        }
                    }
                    catch (ClassNotFoundException excp) {
                        System.out.println("Exception 01: " + excp);
                        //toClient.println("Error!");
                    }
                    catch (SQLException excp2) {
                        System.out.println("Exception 02: " + excp2);
                        //toClient.println("Error!");
                    }
                }
            }
            catch (IOException e) {
                System.out.println("Client disconnected: " +  this.clientSocket.getInetAddress().getHostAddress());
                //e.printStackTrace();
            }
            finally {
                try {
                    if (toClient != null) {
                        toClient.close();
                    }
                    if (fromClient != null) {
                        fromClient.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    //System.out.println("Client disconnected: " +  this.clientSocket.getInetAddress().getHostAddress());
                    //e.printStackTrace();
                }
            }
        }
    }
