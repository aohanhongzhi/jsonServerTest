


import java.sql.*;
import java.util.Vector;

public class Sql {
	 static Connection connection;
	 static  Statement stmt;
	 static String database="anqing";
	 static	String Table="android";
	 static String Table1="todayprice";
	
	public static void connect(){
		 
		   String url = "jdbc:mysql://localhost/"+database+"?user=root&password=newpass&useSSL=true&useUnicode=true&characterEncoding=utf8";
		try {

    		Class.forName("com.mysql.jdbc.Driver");
    		
    		connection = DriverManager.getConnection(url,"root","hxy123");
            System.out.println("\tmysql connected success!");

            stmt = connection.createStatement();
            stmt.executeUpdate("create table if not exists "+Table+"(number varchar(30), name varchar(30),price varchar(30),press varchar(30),date varchar(30),author varchar(30),location varchar(30),time varchar(30))");
            stmt.executeUpdate("create table if not exists "+Table1+"(name varchar(30),price varchar(30),date varchar(30),time varchar(30))");
            
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		
	}
	
	
	
	public static void write(Vector<String>  b){
		String Table="android";
	//	Statement	stmt = null;
		//Connection	 conn = null;
		// conn = ConnectionManager.getInstance().getConnection(); 
		//conn=DBConnectServer.getInstance().getConnection();
//	stmt =conn.createStatement();

			System.out.println("\tBook Writed successful!");
			for(int i=0;i<b.size();i++){
				String[] book=b.get(i).split("#");
				String number =book[0];
				String name =book[1];
				String price =book[2];
				String press =book[3];
				String date =book[4];
				String author =book[5];
				String location =book[6];
				String time =book[7];
	
		   	int rs;
			try{
					ResultSet rs2 = stmt.executeQuery("select * from "+Table+" where number= '"+number+"'");
		//		String sql="select * from "+Table+" where number="+book[m].getNumber();
			//	PreparedStatement stmt=conn.prepareCall(sql);
		      //  ResultSet rs2=stmt.executeQuery(sql);
				//ResultSet rs2 = stmt.executeQuery();
				int flag=0;
				while(rs2.next()){
			   /*********************xia mian pan duan hen duo yu   number jiu shi yi yang************************/
					
            	String number1= rs2.getString("number").trim();
            	//System.out.println("number:"+number1);
            	if(number1.equals(number)){
            		//data is exited!
            		String time1= rs2.getString("time").trim();
            		//System.out.println("time:"+time1);
            		long time2=Long.parseLong(time1);
            		long time3=Long.parseLong(time);//client time
            		if(time2<time3){
            			flag=2;//client is up-to-data!
            	//		System.out.println("client is up-to-data!");
            		}
            		else{
            			flag=3;
            	//		System.out.println("server is up-to-data!");
            			//server is up-to-data.flag=0.
            		}
            	}
            	else{//data is not exist!
            		flag=1;
            	//	System.out.println("data is not exist!");
            	}
            	
            
				}
	        //    System.out.println("flag="+flag);
            if(flag==1||flag==0){
				 rs = stmt.executeUpdate("insert into "+Table+"(number,name,price,press,date,author,location,time) values ('"+number+"','"+name+"','"+price+"','"+press+"','"+date+"','"+author+"','"+location+"','"+time+"')");
            	//   System.out.println("data writed success!");
            }
            else if(flag==2){

            	rs=stmt.executeUpdate("update "+Table+" set name='"+name+"' where number = "+number);
            	rs=stmt.executeUpdate("update "+Table+" set price='"+price+"' where number = "+number);
            	rs=stmt.executeUpdate("update "+Table+" set press='"+press+"' where number = "+number);
            	rs=stmt.executeUpdate("update "+Table+" set date='"+date+"' where number = "+number);
            	rs=stmt.executeUpdate("update "+Table+" set author='"+author+"' where number = "+number);
            	rs=stmt.executeUpdate("update "+Table+" set location='"+location+"' where number = "+number);
            	rs=stmt.executeUpdate("update "+Table+" set time='"+time+"' where number = "+number);
            //	System.out.println("data has updated");
            }else;// if(flag==3)
            	//System.out.println("data has existed!");
            	
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("\tmysql connected failed!");
	        }  
			}
	}
	

	

	public static StringBuffer  read() {
		 StringBuffer sb= new StringBuffer();
		try{
			String Table="android";
		//	stmt.executeUpdate("create table if not exists "+Table+"(number varchar(30), name varchar(30),price varchar(30),press varchar(30),date varchar(30),author varchar(30),location varchar(30))");
      
		   ResultSet rs1 = stmt.executeQuery("select * from "+Table);
		
		   sb.append("bookTable\n");
		   while(rs1.next()){
	         	String number2= rs1.getString("number").trim();
	         	String name2= rs1.getString("name").trim();
	         	String price2= rs1.getString("price").trim();
	         	String press2= rs1.getString("press").trim();
	         	String date2= rs1.getString("date").trim();
	         	String author2= rs1.getString("author").trim();
	        	String location2= rs1.getString("location").trim();
	        	String time2=rs1.getString("time").trim();
	        	if(!number2.equals("null")){// duo yu!
	        		sb.append(number2+"#"+name2+"#"+price2+"#"+press2+"#"+date2+"#"+author2+"#"+location2+"#"+time2+"\n");
	        	}
		   }
		   sb.append("todayPrice\n");
		   ResultSet rs2 = stmt.executeQuery("select * from  TodayPrice");
		   while(rs2.next()){
			   String name= rs2.getString("name").trim();
			   String price= rs2.getString("price").trim();
			   String date= rs2.getString("date").trim();
			   String time= rs2.getString("time").trim();
			   sb.append(name+"\n"+price+"\n"+date+"\n"+time+"\n");
			   
		   }
      
         	System.out.println("\tdata has send successful!");
      
     } catch (SQLException e) {
         e.printStackTrace();
         System.out.println("\tmysql connected failed!");
     }  
		
		return sb;
	
	}



	public static void write_todayPrice(Vector<String> v) {
		//System.out.println("TodayPrice write to mysql"+v.size());
		
	
		
			try {
				for(int i = 0; i<v.size();i++){
					String a[] =v.get(i).split("#");
					String name=a[0].trim();
					String price=a[1];
					String date=a[2];
					String time=a[3];
					//System.out.println(i+":"+name+"/"+price+date+time);
					ResultSet rs3 = stmt.executeQuery("select * from "+Table1+" where name = '"+name+"'");// only number
					int flag=0;
					while(rs3.next()){
						flag++;
					
		        	String time1= rs3.getString("time").trim();
		        	long time2= Long.parseLong(time1);
		        	long time3=Long.parseLong(time);
		        	if(time3>time2){
		        		stmt.executeUpdate("update "+Table1+" set date='"+date+"' where name = '"+name+"'");
		        		stmt.executeUpdate("update "+Table1+" set price='"+price+"' where name = '"+name+"'");
		        		stmt.executeUpdate("update "+Table1+" set time='"+time+"' where name = '"+name+"'");
		        	//	System.out.println("TodayPrice update to mysql successfully");
		        		
		        	}
		        	else{
		        //		System.out.println("Server's todayPrice  is up-to-data!!");
		        	
			        	}
		        	}
					if(flag==0){
						stmt.executeUpdate("insert into "+Table1+"(name,price,date,time) values ('"+name+"','"+price+"','"+date+"','"+time+"')");
					//	System.out.println("TodayPrice insert to mysql successfully");
					
					}
				
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally{
				System.out.println("\tTodayPrice writed successful!");
			}
        
		
		
		
		
	}
}
