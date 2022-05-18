import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        System.out.println("welcome to idbc bank, how can we help you");
        System.out.println("please enter the account type to be opened \n 1.PAY ACC \n 2.SAVE ACC ");
        int abc=0;
        int choice;
        choice=s.nextInt();
        if(choice==1)
        {
            do {
                System.out.println("press" +
                        "\n 1.Account Opening" +
                        "\n 2.Balance Enquiry" +
                        "\n 3.Withdrawl" +
                        "\n 4.Deposit" +
                        "\n 5.Fund Transfer" +
                        "\n 6.Mini Statement");
                int ans = s.nextInt();

                switch(ans) {
                    case 1:
                        accOpen();
                        break;
                    case 2:
                        enquiry();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        deposit();
                        break;
                    case 5:
                        fundTransfer();
                        break;
                    case 6:
                        miniStatement();
                        break;
                    default :
                        System.out.println("Please select A Valid Option");

                }
                System.out.println("press 1 for main menu, any other key to exit");
                abc=s.nextInt();
            }while(abc==1);
        }
        if(choice==2)
        {

            do {
                System.out.println("press" +
                        "\n 1.Account Opening" +
                        "\n 2.Balance Enquiry" +
                        "\n 3.Withdrawl" +
                        "\n 4.Deposit" +
                        "\n 5.Fund Transfer" +
                        "\n 6.Calculate Interest");
                int ans = s.nextInt();
                switch(ans) {
                    case 1:
                        accOpen();
                        break;
                    case 2:
                        enquiry();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        deposit();
                        break;
                    case 5:
                        fundTransfer();
                        break;
                    case 6:
                        calculateInterest();
                        break;
                    default :
                        System.out.println("Please select A Valid Option");

                }
                System.out.println("press 1 for main menu");
                abc=s.nextInt();
            }while(abc==1); }


        }

        static void accOpen() {
            Scanner s = new Scanner(System.in);

            System.out.println("Enter Your Age");
            int age = s.nextInt();
            if (age >= 18) {
                try {
                    System.out.println("enter the phone number");
                    int phno = s.nextInt();
                    System.out.println("Enter Your Full Name");
                    String name = s.next();
                    System.out.println("Enter Your Address");
                    String address = s.next();
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "Charan@9");
                    PreparedStatement pst = con.prepareStatement("insert into customer(mobileNo,name,age,address) values(?,?,?,?);");
                    pst.setInt(1, phno);
                    pst.setString(2, name);
                    pst.setInt(3, age);
                    pst.setString(4, address);
                    pst.executeUpdate();
                    int j = 0;
                    ResultSet rs = pst.executeQuery("select cust_id from customer where cust_id=(select max(cust_id)from customer);");
                    while (rs.next()) {
                        j = rs.getInt(1);
                    }
                    System.out.println("enter today's date YY-MM-DD");
                    String date = s.next();
                    System.out.println("Enter the minimum balance to be added in your account");
                    int balance = s.nextInt();
                    while (balance < 500)
                    {
                        System.out.println("please enter the amount greater than or equal to 500");
                        balance = s.nextInt();
                    }
                    if (balance >= 500) {
                        PreparedStatement st = con.prepareStatement("insert into account(acc_type,Dateofcreation,cust_id,Balance) values(?,?,?,?)");
                        st.setString(1, "pay");
                        st.setString(2, date);
                        st.setInt(3, j);
                        st.setInt(4, balance);
                        int i = st.executeUpdate();
                        if (i > 0)
                            System.out.println("congratulations!!! Account Opened SuccessFully");
                        ResultSet rss = st.executeQuery("select acc_no from account where acc_no=(select max(acc_no) from account)");
                        int acc = 0;
                        while (rss.next()) {
                            acc = rss.getInt(1);
                            System.out.println("Your Account Number is " + acc);
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else
                System.out.println("Your Not Eligible To Open An Account, come back When you turn 18");

        }



        static void enquiry()
        {
            System.out.println("Please Enter Your Account Number");
            Scanner sc=new Scanner(System.in);
            int accno=sc.nextInt();
            try
            {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject","root","Charan@9");
               PreparedStatement pst=con.prepareStatement("select balance from account where acc_no=?");
                pst.setInt(1,accno);
                ResultSet rs=pst.executeQuery();
                int amt=0;
                while(rs.next())
                {
                    System.out.println("Your Account Balance is Rs "+rs.getInt(1)+"/-");
                     amt=rs.getInt(1);
                }
                pst=con.prepareStatement("insert into transactions(acc_no,acc_balance,transaction_type,final_balance) values(?,?,?,?)");
                pst.setInt(1,accno);
                pst.setInt(2,amt);
                pst.setString(3,"Balance Enquiry");
                pst.setInt(4,amt);
                pst.executeUpdate();
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
        }
        static void withdraw()
        {
            System.out.println("please enter your Account Number");
            Scanner s=new Scanner(System.in);
            int accno=s.nextInt();
            System.out.println("Enter the amount to be Withdrawn");
            int withdraw=s.nextInt();
            try
            {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject","root","Charan@9");
                PreparedStatement pst=con.prepareStatement("update  account set balance=balance-? where acc_no=?");
                pst.setInt(1,withdraw);
                pst.setInt(2,accno);
                int i=pst.executeUpdate();
                if(i>0)
                {
                    System.out.println("An amount of Rs "+withdraw+"/- withdrawn successfully");
                }
                 PreparedStatement st=con.prepareStatement("select balance from account where acc_no=?");
                st.setInt(1,accno);
                ResultSet rs=st.executeQuery();
                int amt=0;
                while(rs.next())
                {
                    System.out.println("Your updated account balance is rs "+rs.getInt(1)+"/-");
                    amt=rs.getInt(1);
                }
                pst=con.prepareStatement("insert into transactions(acc_no,acc_balance,transaction_type,withdraw,final_balance) values(?,?,?,?,?)");
                pst.setInt(1,accno);
                pst.setInt(2,amt+withdraw);
                pst.setString(3,"Debit");
                pst.setInt(4,withdraw);
                pst.setInt(5,amt);
                pst.executeUpdate();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        static void deposit()
        {
            Scanner s=new Scanner(System.in);
            System.out.println("Please Enter The Account Number");
            int accno=s.nextInt();
            System.out.println("Please Enter Amount To be Deposited");
            int deposit=s.nextInt();
            try
            {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject","root","Charan@9");
                PreparedStatement pst=con.prepareStatement("update  account set balance=balance+? where acc_no=?");
                pst.setInt(1,deposit);
                pst.setInt(2,accno);
                int i=pst.executeUpdate();
                if(i>0)
                {
                    System.out.println("Amount of rs "+deposit+"/- deposited Successfully ");
                }
                PreparedStatement st=con.prepareStatement("select balance from account where acc_no=?");
                st.setInt(1,accno);
                ResultSet rs=st.executeQuery();
                int amt=0;
                while(rs.next())
                {
                    amt=rs.getInt(1);
                    System.out.println("Your updated account balance is rs "+rs.getInt(1)+"/-");
                }
                pst=con.prepareStatement("insert into transactions(acc_no,acc_balance,transaction_type,deposit,final_balance) values(?,?,?,?,?)");
                pst.setInt(1,accno);
                pst.setInt(2,amt-deposit);
                pst.setString(3,"Credit");
                pst.setInt(4,deposit);
                pst.setInt(5,amt);
                pst.executeUpdate();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        static void fundTransfer()
        {
            System.out.println("Please Enter Your Account Number");
            Scanner sc=new Scanner(System.in);
            int accno=sc.nextInt();
            System.out.println("Please Enter the Account Number to whom you want to pay");
            int accno1=sc.nextInt();
            System.out.println("Please Enter the Amount to be Transferred");
            int transferamt=sc.nextInt();
            try
            {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject","root","Charan@9");
                PreparedStatement pst=con.prepareStatement("select balance from account where acc_no=?");
                pst.setInt(1,accno);
                ResultSet rs=pst.executeQuery();
                int bal=0;
                while(rs.next())
                {
                    bal=rs.getInt(1);
                }
                if(bal<transferamt)
                {
                    System.out.println("No Sufficient Funds To Transfer");
                }
                else
                {
                     pst=con.prepareStatement("update account set balance=balance-? where acc_no="+accno);
                     pst.setInt(1,transferamt);
                     int i=pst.executeUpdate();
                     pst=con.prepareStatement("update account set balance=balance+? where acc_no="+accno1);
                     pst.setInt(1,transferamt);
                     int j=pst.executeUpdate();
                     if(i>0 && j>0)
                         System.out.println("Transferred the amount of Rs "+transferamt+"/- Successfully");
                     pst=con.prepareStatement("select balance from account where acc_no="+accno);
                     ResultSet rst=pst.executeQuery();
                     int amt=0;
                     while(rst.next())
                     {
                        amt=rst.getInt(1);
                     }
                    pst=con.prepareStatement("insert into transactions(acc_no,acc_balance,transaction_type,transfer,final_balance) values(?,?,?,?,?)");
                    pst.setInt(1,accno);
                    pst.setInt(2,amt-transferamt);
                    pst.setString(3,"Transferred");
                    pst.setInt(4,transferamt);
                    pst.setInt(5,amt);
                    pst.executeUpdate();
                    pst=con.prepareStatement("select balance from account where acc_no="+accno);
                    ResultSet rst1=pst.executeQuery();
                    int amtt=0;
                    while(rst1.next())
                    {
                        amtt=rst1.getInt(1);
                    }
                    pst=con.prepareStatement("insert into transactions(acc_no,acc_balance,transaction_type,deposit,final_balance) values(?,?,?,?,?)");
                    pst.setInt(1,accno1);
                    pst.setInt(2,amtt-transferamt);
                    pst.setString(3,"credit");
                    pst.setInt(4,transferamt);
                    pst.setInt(5,amtt);
                    pst.executeUpdate();

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        static void calculateInterest()
        {
            System.out.println("Please Enter Your Account Number");
            Scanner s=new Scanner(System.in);
            int accno=s.nextInt();
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "Charan@9");
                PreparedStatement preparedStatement= con.prepareStatement("select balance from account where acc_no="+accno);
                ResultSet rs=preparedStatement.executeQuery();
                double interest=0;
                int amt=0;
                while(rs.next())
                {
                    amt=rs.getInt(1);
                    interest=rs.getInt(1)*(2.5/100);
                }
                System.out.println("Your Interest Amount is "+interest+"/-");

                preparedStatement=con.prepareStatement("insert into transactions(acc_no,acc_balance,transaction_type,interest_amt,final_balance) values(?,?,?,?,?)");
                preparedStatement.setInt(1,accno);
                preparedStatement.setInt(2,amt);
                preparedStatement.setString(3,"Calculate Interest");
                preparedStatement.setDouble(4, (int)interest);
                preparedStatement.setInt(5,amt+(int)interest);
                preparedStatement.executeUpdate();
                System.out.println("your Updated Balance is "+(amt+(int)interest));

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
        static void miniStatement()
        {
            try
            {
                System.out.println("Please Enter Your Account Number");
                Scanner s=new Scanner(System.in);
                int accno=s.nextInt();
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "Charan@9");
                PreparedStatement pst=con.prepareStatement("select * from transactions where acc_no="+accno);
                ResultSet rs=pst.executeQuery();
                while(rs.next())
                {
                    if(rs.getString(4).equalsIgnoreCase("Balance Enquiry"))
                    {
                        System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getString(4)+" "+rs.getInt(9));
                        // System.out.format("%d, %d, %d, %s, %d"+rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getInt(9));
                    }
                     if(rs.getString(4).equalsIgnoreCase("credit"))
                    {
                        System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getInt(3)+" "+rs.getString(4)+" "+rs.getInt(6)+" "+rs.getInt(9));
                    }
                    if(rs.getString(4).equalsIgnoreCase("debit"))
                    {
                        System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getInt(3)+" "+rs.getString(4)+" "+rs.getInt(5)+" "+rs.getInt(9));
                    }
                    if(rs.getString(4).equalsIgnoreCase("transferred"))
                    {
                        System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getInt(3)+" "+rs.getString(4)+" "+rs.getInt(8)+" "+rs.getInt(9));
                    }
                    if(rs.getString(4).equalsIgnoreCase("Calculate Interest"))
                    {
                        System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getInt(3)+" "+rs.getString(4)+" "+rs.getInt(7)+" "+rs.getInt(9));
                    }

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }


