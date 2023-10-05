package neu.edu.webapp.DAO;

import neu.edu.webapp.Util.HibernateUtil;
import neu.edu.webapp.Util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DAO {


	private static final Logger log = Logger.getAnonymousLogger();
	private static final ThreadLocal sessionThread = new ThreadLocal();
	private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	protected DAO() {
	}

	public static Session getSession() {
		Session session = (Session) DAO.sessionThread.get();

		if (session == null) {
			session = sessionFactory.openSession();
			DAO.sessionThread.set(session);
		}
		return session;
	}

	protected void begin() {
		getSession().beginTransaction();
	}

	protected void commit() {
		getSession().getTransaction().commit();
	}

	protected void rollback() {
		try {
			getSession().getTransaction().rollback();
		} catch (HibernateException e) {
			log.log(Level.WARNING, "Cannot rollback", e);
		}
		try {
			getSession().close();
		} catch (HibernateException e) {
			log.log(Level.WARNING, "Cannot close", e);
		}
		DAO.sessionThread.set(null);
	}

	public static void close() {
		getSession().close();
		DAO.sessionThread.set(null);
	}










//	private static final Logger messageLog = Logger.getAnonymousLogger();
//	private static final ThreadLocal sessThreadLocal = new ThreadLocal();
//	private static SessionFactory sessFact; // initializing session factory
//
//	protected DAO() {
//	}
//
//	public static Session getNewSession() {
//		Session newSession = (Session) DAO.sessThreadLocal.get();
//
//		if (newSession == null) {
//			try {
//				if (sessFact == null) {
//                    // Create the sessionFactory if it is null
//					sessFact = HibernateUtil.getSessionFactory();
//                }
//				newSession = sessFact.openSession();
//				DAO.sessThreadLocal.set(newSession);
//			} catch (Exception e) {
//
//				e.printStackTrace();
//			}
//		}
//		return newSession;
//	}
//
//	protected void begin() {
//		if (getNewSession() != null) {
//			try {
//				getNewSession().beginTransaction();
//			} catch (HibernateException e) {
//
//				e.getMessage();
//			}
//
//		}
//
//	}
//
//	protected void commit() {
//		if (getNewSession() != null) {
//			try {
//				getNewSession().getTransaction().commit();
//			} catch (HibernateException e) {
//
//				e.getMessage();
//			}
//
//		}
//
//	}
//
//	protected void rollback() {
//		try {
//			getNewSession().getTransaction().rollback();
//		} catch (HibernateException e) {
//			messageLog.log(Level.WARNING, "Cannot rollback", e);
//		}
//		try {
//			getNewSession().close();
//		} catch (HibernateException e) {
//			messageLog.log(Level.WARNING, "Cannot close", e);
//		}
//		DAO.sessThreadLocal.set(null);
//	}
//
//	public static void close() {
//		if (getNewSession() != null) {
//			try {
//				getNewSession().close();
//			} catch (HibernateException e) {
//
//				e.getMessage();
//			}
//
//		}
//
//		DAO.sessThreadLocal.set(null);
//	}





}
