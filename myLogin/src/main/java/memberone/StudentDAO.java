package memberone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class StudentDAO {
	private static StudentDAO instance = null;
	private StudentDAO() {}
	public static StudentDAO getInstance() {
		if(instance ==null) {
			synchronized(StudentDAO.class) {
				instance = new StudentDAO();
			}
		}
		return instance;
	}
	
	
	
	
	
	private Connection getConnection() {
		Connection conn = null;
		try {
			Context init = new InitialContext();
			DataSource ds = 
				(DataSource)init.lookup("java:comp/env/jdbc/myOracle");
			conn = ds.getConnection();
			
		}catch(Exception e) {
			System.err.println("Connection 생성실패");
		}
		return conn;
	}
	
	public boolean idCheck(String id) {
		boolean result = true;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(
					"select * from student where id = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(!rs.next()) result = false;		
			System.out.println(result);
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			if(rs != null) try{rs.close();}catch(SQLException sqle1) {}
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle2) {}
			if(conn != null) try{conn.close();}catch(SQLException sqle3) {}
		}
		
		return result;
	}
	
	public Vector<ZipCodeVO> zipcodeRead(String dong){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Vector<ZipCodeVO> vecList = 
				new Vector<ZipCodeVO>();
		
		try {
			conn = getConnection();
			String strQuery = 
					"select * from zipcode where dong like '" + dong + "%'";
			pstmt = conn.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ZipCodeVO tempZipcode = new ZipCodeVO();
				tempZipcode.setZipcode(rs.getString("zipcode"));
				tempZipcode.setSido(rs.getString("sido"));
				tempZipcode.setGugun(rs.getString("gugun"));
				tempZipcode.setDong(rs.getString("dong"));
				tempZipcode.setRi(rs.getString("ri"));
				tempZipcode.setBunji(rs.getString("bunji"));
				
				vecList.add(tempZipcode);
			}
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			if(rs != null) try{rs.close();}catch(SQLException sqle1) {}
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle2) {}
			if(conn != null) try{conn.close();}catch(SQLException sqle3) {}
		}
		
		return vecList;
	}

	public boolean memberInsert(StudentVO vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		
		
		try {
			conn = getConnection();

			String strQuery = "insert into student values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getPhone1());
			pstmt.setString(5, vo.getPhone2());
			pstmt.setString(6, vo.getPhone3());
			pstmt.setString(7, vo.getEmail());
			pstmt.setString(8, vo.getZipcode());
			pstmt.setString(9, vo.getAddress1());
			pstmt.setString(10, vo.getAddress2());
			int count = pstmt.executeUpdate();
			if(count>0) {flag = true;}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try{rs.close();}catch(SQLException sqle1) {}
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle2) {}
			if(conn != null) try{conn.close();}catch(SQLException sqle3) {}
		}
		
		return flag;
	}
	
	//db에서 id와 password를 비교하여 그 결과값을 정수형으로 리턴해주는 메소드
	public int loginCheck(String id, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int check = -1; //아이디 없음
		try {
			conn = getConnection();
			String strQuery = "select pass from student where id = ?";
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String dbPass = rs.getString("pass");
				if(pass.equals(dbPass)) check=1;
				else check=0;
			}
		} catch (Exception ex) {
			System.out.println("Exception"+ex);
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException sqle1) {}
			if(pstmt!=null)try {pstmt.close();}catch(SQLException sqle2) {}
			if(conn!=null)try {conn.close();}catch(SQLException sqle3) {}
		}
		return check;
	}
	//회원정보를 얻어올 메소드
	public StudentVO getMember(String id) {
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		StudentVO vo = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from student where id = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				vo = new StudentVO();
				vo.setId(rs.getString("id"));
				vo.setPass(rs.getString("pass"));
				vo.setName(rs.getString("name"));
				vo.setPhone1(rs.getString("phone1"));
				vo.setPhone2(rs.getString("phone2"));
				vo.setPhone3(rs.getString("phone3"));
				vo.setEmail(rs.getString("email"));
				vo.setZipcode(rs.getString("zipcode"));
				vo.setAddress1(rs.getString("Address1"));
				vo.setAddress2(rs.getString("Address2"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException sqle1) {}
			if(pstmt!=null)try {pstmt.close();}catch(SQLException sqle2) {}
			if(conn!=null)try {conn.close();}catch(SQLException sqle3) {}
		}
		return vo;
	}
	
	//정보수정을 처리해줄 매서드 
	public void updateMember(StudentVO vo) {
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("update student set pass=?,phone1=?,phone2=?,phone3=?,email=?,zipcode=?,address1=?,address2=? where id=?");
			pstmt.setString(1, vo.getPass());
			pstmt.setString(2, vo.getPhone1());
			pstmt.setString(3, vo.getPhone2());
			pstmt.setString(4, vo.getPhone3());
			pstmt.setString(5, vo.getEmail());
			pstmt.setString(6, vo.getZipcode());
			pstmt.setString(7, vo.getAddress1());
			pstmt.setString(8, vo.getAddress2());
			pstmt.setString(9, vo.getId());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			if(pstmt!=null)try {pstmt.close();}catch(SQLException sqle2) {}
			if(conn!=null)try {conn.close();}catch(SQLException sqle3) {}
		}
	}
	//DB에서 회원 삭제를 처리해줄 메서드
	public int deleteMember(String id, String pass) {
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		String dbPass="";
		int result=-1;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from student where id = ?");
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dbPass =rs.getString("pass");
				if(dbPass.equals(pass)) {
					pstmt=conn.prepareStatement("delete from student where id = ?");
					pstmt.setString(1, id);
					pstmt.executeUpdate();
					result=1;
				}else {
					result=0;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException sqle1) {}
			if(pstmt!=null)try {pstmt.close();}catch(SQLException sqle2) {}
			if(conn!=null)try {conn.close();}catch(SQLException sqle3) {}
		}
		return result;
	}
}
