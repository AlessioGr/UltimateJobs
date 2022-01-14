package de.warsteiner.jobs.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.statements.SQLStatementAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;

public class SQLManager {

	public void createtables() {
		SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI();
		UltimateJobs.getPlugin().getExecutor().execute(() -> {

			s.executeUpdate("CREATE TABLE IF NOT EXISTS job_playerlist (UUID varchar(200), NAME varchar(200))");
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_stats (UUID varchar(200), JOB varchar(200), DATE varchar(200), LEVEL int, EXP double, BROKEN int)");

			s.executeUpdate("CREATE TABLE IF NOT EXISTS job_current (UUID varchar(200), JOB varchar(200))");
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_players (UUID varchar(200), DATE varchar(200), POINTS int, MAX int)");
		});
	}

	public void savePlayer(JobsPlayer pl, String UUID) {
		SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI();
		 

		s.executeUpdate("DELETE FROM job_stats WHERE UUID='" + UUID + "';");
		s.executeUpdate("DELETE FROM job_players WHERE UUID='" + UUID + "';"); 
		s.executeUpdate("DELETE FROM job_current WHERE UUID='" + UUID + "';");

		List<String> current = pl.getCurrentJobs();
		List<String> owned = pl.getOwnJobs();

		int max = pl.getMaxJobs();
		double points = pl.getPoints();

		  
		final String insertQuery_player = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
		s.executeUpdate(insertQuery_player, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, UltimateJobs.getPlugin().getAPI().getDate());
			ps.setDouble(3, points);
			ps.setInt(4, max);
		
		});

		 

		final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";

		for (String job : owned) { 
			int level = pl.getLevelOf(job);
			double exp = pl.getExpOf(job);
			int broken = pl.getBrokenOf(job);
			String date = pl.getDateOfJob(job);

			s.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, job);
				ps.setString(3, date);
				ps.setInt(4, level);
				ps.setDouble(5, exp);
				ps.setInt(6, broken);
			
			});

		}
 
		final String insertQuery_owned = "INSERT INTO job_current(UUID,JOB) VALUES(?,?)";

		for (String job : current) {
		 
			s.executeUpdate(insertQuery_owned, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, job);
			
			});
		}

		UltimateJobs.getPlugin().doLog("INFO", "§9Saved Data of UUID: " + UUID);

	}

	public void createJobData(String UUID, String job) {
		String date = UltimateJobs.getPlugin().getAPI().getDate();
		SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI();
		final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";
		s.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, job);
			ps.setString(3, date);
			ps.setInt(4, 1);
			ps.setDouble(5, 0);
			ps.setInt(6, 0);
		
		});
		UltimateJobs.getPlugin().doLog("INFO", "§9Created Data of UUID: " + UUID + " of job : " + job);
	}
 
	public boolean ExistJobData(String UUID, String job) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicReference<String> a = new AtomicReference<String>();
 
         s.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getString("DATE"));
             }  
              return 1;
         });
         return a.get() != null;
    }
	
	public int getLevelOf(String UUID, String job) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicInteger a = new AtomicInteger(0);
 
         s.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getInt("LEVEL"));
             }  
              return 1;
         });
         return a.get();
    }
	
	public double getExpOf(String UUID, String job) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicDouble a = new AtomicDouble();

         s.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getDouble("EXP"));
             }  
              return 0;
         });
         return a.get();
    }
	
	public int getBrokenOf(String UUID, String job) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicInteger a = new AtomicInteger(0);
 
         s.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getInt("BROKEN"));
             }  
              return 0;
         });
         return a.get();
    }
	
	public String getDateOf(String UUID, String job) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicReference<String> a = new AtomicReference<String>();
 
         s.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getString("DATE"));
             }  
              return 0;
         });
         return a.get();
    }
  
	public ArrayList<String> getOwnedJobs(String UUID) {
		ArrayList<String> jobs = new ArrayList<String>();
		try {
			ResultSet rs = Query("SELECT * FROM job_stats WHERE UUID= '" + UUID + "'");
			if (rs != null) { 
					while (rs.next()) {
						jobs.add(rs.getString("JOB"));
					}
			 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobs;
	}

	public ArrayList<String> getCurrentJobs(String UUID) {
		ArrayList<String> jobs = new ArrayList<String>();
		try {
			ResultSet rs = Query("SELECT * FROM job_current WHERE UUID= '" + UUID + "'");
			if (rs != null) { 
					while (rs.next()) {
						jobs.add(rs.getString("JOB"));
					}
			 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobs;
	}
 
	public int getPoints(String UUID) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicInteger a = new AtomicInteger(0);
 
         s.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getInt("POINTS"));
             }  
              return 0;
         });
         return a.get();
    }
	
	public int getMaxJobs(String UUID) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicInteger a = new AtomicInteger(0);
 
         s.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getInt("MAX"));
             }  
              return 0;
         });
         return a.get();
    }
  
	public void createPlayer(String UUID, String name) {
		String date = UltimateJobs.getPlugin().getAPI().getDate();
		createPlayerDetails(UUID, date);
		createPlayerInList(UUID, name);
	}

	public void createPlayerInList(String UUID, String name) {
		SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI();
		final String insertQuery = "INSERT INTO job_playerlist(UUID,NAME) VALUES(?,?)";
		s.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, name);
		
		});
	}

	public void createPlayerDetails(String UUID, String date) {
		SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI();
		int max = UltimateJobs.getPlugin().getMainConfig().getConfig().getInt("MaxDefaultJobs");
		final String insertQuery = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
		s.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, "" + date);
			ps.setInt(3, 0);
			ps.setInt(4, max);
		
		});
	}
 
	public String getUUIDFromName(String NAME) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicReference<String> a = new AtomicReference<String>();
 
         s.executeQuery("SELECT * FROM job_playerlist WHERE NAME= '" + NAME + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getString("UUID"));
             }  
              return 0;
         });
         return a.get();
    }
	
	public String getNameFromUUID(String UUID) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicReference<String> a = new AtomicReference<String>();
 
         s.executeQuery("SELECT * FROM job_playerlist WHERE UUID= '" + UUID + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getString("NAME"));
             }  
              return 0;
         });
         return a.get();
    }
 
	public boolean ExistPlayer(String UUID) {
        SQLStatementAPI s = UltimateAPI.getInstance().getSQLStatementAPI(); 
        AtomicReference<String> a = new AtomicReference<String>();
 
         s.executeQuery("SELECT * FROM job_playerlist WHERE UUID= '" + UUID + "'", rs -> {
              if (rs.next()) { 
            	  a.set(rs.getString("NAME"));
             }  
              return 1;
         });
         return a.get() != null;
    }
 
	public ResultSet Query(String q) {
		ResultSet rs = null;
		try {
			Statement t = UltimateJobs.getPlugin().getConnection();
			rs = t.executeQuery(q); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

}
