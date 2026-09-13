from typing import List, Optional
from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.models.career_profile import CareerProfile
from app.models.personal_info import PersonalInfo
from app.models.education import Education
from app.models.skill import Skill
from app.models.experience import Experience
from app.models.project import Project
from app.models.certification import Certification
from app.models.social_profile import SocialProfile
from app.models.job_preference import JobPreference
from app.schemas.profile import (
    CareerProfileUpdate,
    PersonalInfoCreate,
    EducationCreate,
    EducationUpdate,
    SkillCreate,
    ExperienceCreate,
    ExperienceUpdate,
    ProjectCreate,
    ProjectUpdate,
    CertificationCreate,
    SocialProfileCreate,
    JobPreferenceCreate
)


class ProfileService:
    @staticmethod
    def get_or_create_profile(db: Session, user_id: str) -> CareerProfile:
        """Fetches the user's career profile or creates a new one if missing."""
        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user_id).first()
        if not profile:
            profile = CareerProfile(
                user_id=user_id,
                headline="Software Engineer & Technologist",
                summary="Passionate engineer building solutions with modern technologies.",
                profile_strength=50
            )
            db.add(profile)
            db.commit()
            db.refresh(profile)
        return profile

    @staticmethod
    def calculate_profile_strength(profile: CareerProfile) -> int:
        """
        Dynamically calculates profile completeness percentage (0 to 100).
        """
        score = 20  # Base account creation score
        if profile.personal_info and profile.personal_info.phone and profile.personal_info.location:
            score += 15
        if profile.education and len(profile.education) > 0:
            score += 15
        if profile.skills and len(profile.skills) >= 3:
            score += 20
        if profile.projects and len(profile.projects) >= 1:
            score += 15
        if profile.experience and len(profile.experience) >= 1:
            score += 10
        if profile.social_profiles and len(profile.social_profiles) >= 1:
            score += 5
        return min(score, 100)

    @classmethod
    def update_profile(cls, db: Session, user_id: str, profile_in: CareerProfileUpdate) -> CareerProfile:
        profile = cls.get_or_create_profile(db, user_id)
        if profile_in.headline is not None:
            profile.headline = profile_in.headline
        if profile_in.summary is not None:
            profile.summary = profile_in.summary
        
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(profile)
        return profile

    # --- Personal Info ---
    @classmethod
    def update_personal_info(cls, db: Session, user_id: str, info_in: PersonalInfoCreate) -> PersonalInfo:
        profile = cls.get_or_create_profile(db, user_id)
        personal_info = db.query(PersonalInfo).filter(PersonalInfo.profile_id == profile.id).first()
        if not personal_info:
            personal_info = PersonalInfo(profile_id=profile.id, **info_in.model_dump())
            db.add(personal_info)
        else:
            for field, val in info_in.model_dump(exclude_unset=True).items():
                setattr(personal_info, field, val)
        
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(personal_info)
        return personal_info

    # --- Education CRUD ---
    @classmethod
    def add_education(cls, db: Session, user_id: str, edu_in: EducationCreate) -> Education:
        profile = cls.get_or_create_profile(db, user_id)
        edu = Education(profile_id=profile.id, **edu_in.model_dump())
        db.add(edu)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(edu)
        return edu

    @classmethod
    def update_education(cls, db: Session, user_id: str, edu_id: str, edu_in: EducationUpdate) -> Education:
        profile = cls.get_or_create_profile(db, user_id)
        edu = db.query(Education).filter(Education.id == edu_id, Education.profile_id == profile.id).first()
        if not edu:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Education record not found.")
        for field, val in edu_in.model_dump(exclude_unset=True).items():
            setattr(edu, field, val)
        db.commit()
        db.refresh(edu)
        return edu

    @classmethod
    def delete_education(cls, db: Session, user_id: str, edu_id: str) -> None:
        profile = cls.get_or_create_profile(db, user_id)
        edu = db.query(Education).filter(Education.id == edu_id, Education.profile_id == profile.id).first()
        if not edu:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Education record not found.")
        db.delete(edu)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()

    # --- Skills CRUD ---
    @classmethod
    def add_skill(cls, db: Session, user_id: str, skill_in: SkillCreate) -> Skill:
        profile = cls.get_or_create_profile(db, user_id)
        # Check duplicate
        existing = db.query(Skill).filter(
            Skill.profile_id == profile.id,
            Skill.name.ilike(skill_in.name.strip())
        ).first()
        if existing:
            return existing
        skill = Skill(profile_id=profile.id, **skill_in.model_dump())
        db.add(skill)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(skill)
        return skill

    @classmethod
    def delete_skill(cls, db: Session, user_id: str, skill_id: str) -> None:
        profile = cls.get_or_create_profile(db, user_id)
        skill = db.query(Skill).filter(Skill.id == skill_id, Skill.profile_id == profile.id).first()
        if not skill:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Skill record not found.")
        db.delete(skill)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()

    # --- Projects CRUD ---
    @classmethod
    def add_project(cls, db: Session, user_id: str, project_in: ProjectCreate) -> Project:
        profile = cls.get_or_create_profile(db, user_id)
        proj = Project(profile_id=profile.id, **project_in.model_dump())
        db.add(proj)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(proj)
        return proj

    @classmethod
    def update_project(cls, db: Session, user_id: str, project_id: str, project_in: ProjectUpdate) -> Project:
        profile = cls.get_or_create_profile(db, user_id)
        proj = db.query(Project).filter(Project.id == project_id, Project.profile_id == profile.id).first()
        if not proj:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Project not found.")
        for field, val in project_in.model_dump(exclude_unset=True).items():
            setattr(proj, field, val)
        db.commit()
        db.refresh(proj)
        return proj

    @classmethod
    def delete_project(cls, db: Session, user_id: str, project_id: str) -> None:
        profile = cls.get_or_create_profile(db, user_id)
        proj = db.query(Project).filter(Project.id == project_id, Project.profile_id == profile.id).first()
        if not proj:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Project not found.")
        db.delete(proj)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()

    # --- Experience CRUD ---
    @classmethod
    def add_experience(cls, db: Session, user_id: str, exp_in: ExperienceCreate) -> Experience:
        profile = cls.get_or_create_profile(db, user_id)
        exp = Experience(profile_id=profile.id, **exp_in.model_dump())
        db.add(exp)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(exp)
        return exp

    @classmethod
    def update_experience(cls, db: Session, user_id: str, exp_id: str, exp_in: ExperienceUpdate) -> Experience:
        profile = cls.get_or_create_profile(db, user_id)
        exp = db.query(Experience).filter(Experience.id == exp_id, Experience.profile_id == profile.id).first()
        if not exp:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Experience record not found.")
        for field, val in exp_in.model_dump(exclude_unset=True).items():
            setattr(exp, field, val)
        db.commit()
        db.refresh(exp)
        return exp

    @classmethod
    def delete_experience(cls, db: Session, user_id: str, exp_id: str) -> None:
        profile = cls.get_or_create_profile(db, user_id)
        exp = db.query(Experience).filter(Experience.id == exp_id, Experience.profile_id == profile.id).first()
        if not exp:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Experience record not found.")
        db.delete(exp)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()

    # --- Certifications CRUD ---
    @classmethod
    def add_certification(cls, db: Session, user_id: str, cert_in: CertificationCreate) -> Certification:
        profile = cls.get_or_create_profile(db, user_id)
        cert = Certification(profile_id=profile.id, **cert_in.model_dump())
        db.add(cert)
        db.commit()
        db.refresh(cert)
        return cert

    @classmethod
    def delete_certification(cls, db: Session, user_id: str, cert_id: str) -> None:
        profile = cls.get_or_create_profile(db, user_id)
        cert = db.query(Certification).filter(Certification.id == cert_id, Certification.profile_id == profile.id).first()
        if not cert:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Certification not found.")
        db.delete(cert)
        db.commit()

    # --- Social Profiles CRUD ---
    @classmethod
    def add_social_profile(cls, db: Session, user_id: str, social_in: SocialProfileCreate) -> SocialProfile:
        profile = cls.get_or_create_profile(db, user_id)
        social = SocialProfile(profile_id=profile.id, **social_in.model_dump())
        db.add(social)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()
        db.refresh(social)
        return social

    @classmethod
    def delete_social_profile(cls, db: Session, user_id: str, social_id: str) -> None:
        profile = cls.get_or_create_profile(db, user_id)
        social = db.query(SocialProfile).filter(SocialProfile.id == social_id, SocialProfile.profile_id == profile.id).first()
        if not social:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Social profile not found.")
        db.delete(social)
        profile.profile_strength = cls.calculate_profile_strength(profile)
        db.commit()

    # --- Job Preferences ---
    @classmethod
    def set_job_preference(cls, db: Session, user_id: str, pref_in: JobPreferenceCreate) -> JobPreference:
        profile = cls.get_or_create_profile(db, user_id)
        pref = db.query(JobPreference).filter(JobPreference.profile_id == profile.id).first()
        if not pref:
            pref = JobPreference(profile_id=profile.id, **pref_in.model_dump())
            db.add(pref)
        else:
            for field, val in pref_in.model_dump(exclude_unset=True).items():
                setattr(pref, field, val)
        db.commit()
        db.refresh(pref)
        return pref
