"""
Safe Demo Data Cleanup Script for JobPilot.
Safely purges test/demo candidate records, mock applications, and obsolete test resumes
WITHOUT dropping tables, credentials, or Render PostgreSQL database infrastructure.
"""

import sys
import os
import logging

# Ensure backend root is on Python path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..")))

from sqlalchemy.orm import Session
from app.core.database import SessionLocal
from app.models.user import User
from app.models.resume import Resume
from app.models.application import Application
from app.models.job_match import JobMatch
from app.models.connected_account import ConnectedAccount
from app.models.otp import EmailOtp

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("jobpilot.cleanup")


def clean_demo_data():
    db: Session = SessionLocal()
    try:
        logger.info("Starting safe demo data cleanup...")

        # 1. Target test accounts (example.com, test*, or chetan.student)
        test_patterns = ["%@example.com", "%test%", "chetan.student@gmail.com"]
        test_users = []
        for pattern in test_patterns:
            users = db.query(User).filter(User.email.ilike(pattern)).all()
            for u in users:
                if u not in test_users:
                    test_users.append(u)

        logger.info(f"Identified {len(test_users)} test/demo accounts for cleanup.")

        for user in test_users:
            logger.info(f"Purging test user data: {user.email} (ID: {user.id})")
            db.delete(user)

        # 2. Invalidate obsolete test OTP records
        db.query(EmailOtp).filter(EmailOtp.email.ilike("%@example.com")).delete(synchronize_session=False)

        db.commit()
        logger.info("Safe demo data cleanup completed successfully. Schema and production data preserved.")
    except Exception as e:
        db.rollback()
        logger.error(f"Cleanup encountered error: {e}")
        raise
    finally:
        db.close()


if __name__ == "__main__":
    clean_demo_data()
