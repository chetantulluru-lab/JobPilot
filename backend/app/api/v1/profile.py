import os
import io
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status, UploadFile, File
from fastapi.responses import FileResponse
from PIL import Image
from sqlalchemy.orm import Session
from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.schemas.profile import (
    CareerProfileResponse,
    CareerProfileUpdate,
    PersonalInfoCreate,
    PersonalInfoUpdate,
    PersonalInfoResponse,
    EducationCreate,
    EducationUpdate,
    EducationResponse,
    SkillCreate,
    SkillResponse,
    ExperienceCreate,
    ExperienceUpdate,
    ExperienceResponse,
    ProjectCreate,
    ProjectUpdate,
    ProjectResponse,
    CertificationCreate,
    CertificationResponse,
    SocialProfileCreate,
    SocialProfileResponse,
    JobPreferenceCreate,
    JobPreferenceResponse
)
from app.services.profile_service import ProfileService

router = APIRouter(prefix="/profile", tags=["Career Profile"])


@router.get("", response_model=CareerProfileResponse)
def get_my_profile(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieve the full career profile for the authenticated user."""
    return ProfileService.get_or_create_profile(db, current_user.id)


@router.put("", response_model=CareerProfileResponse)
def update_profile_summary(
    profile_in: CareerProfileUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Update high-level career headline and summary."""
    return ProfileService.update_profile(db, current_user.id, profile_in)


@router.put("/personal-info", response_model=PersonalInfoResponse)
def update_personal_info(
    info_in: PersonalInfoUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Update contact information and personal details."""
    return ProfileService.update_personal_info(db, current_user.id, info_in)


# --- Education Endpoints ---
@router.post("/education", response_model=EducationResponse, status_code=status.HTTP_201_CREATED)
def add_education(
    edu_in: EducationCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Add an academic degree or coursework to career profile."""
    return ProfileService.add_education(db, current_user.id, edu_in)


@router.put("/education/{edu_id}", response_model=EducationResponse)
def update_education(
    edu_id: str,
    edu_in: EducationUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Update an existing education entry."""
    return ProfileService.update_education(db, current_user.id, edu_id, edu_in)


@router.delete("/education/{edu_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_education(
    edu_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Remove an education entry."""
    ProfileService.delete_education(db, current_user.id, edu_id)
    return None


# --- Skill Endpoints ---
@router.post("/skills", response_model=SkillResponse, status_code=status.HTTP_201_CREATED)
def add_skill(
    skill_in: SkillCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Add a verified or stated technical/soft skill."""
    return ProfileService.add_skill(db, current_user.id, skill_in)


@router.delete("/skills/{skill_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_skill(
    skill_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Delete a skill from career profile."""
    ProfileService.delete_skill(db, current_user.id, skill_id)
    return None


# --- Projects Endpoints ---
@router.post("/projects", response_model=ProjectResponse, status_code=status.HTTP_201_CREATED)
def add_project(
    project_in: ProjectCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Add a portfolio project or academic capstone."""
    return ProfileService.add_project(db, current_user.id, project_in)


@router.put("/projects/{project_id}", response_model=ProjectResponse)
def update_project(
    project_id: str,
    project_in: ProjectUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Update project details."""
    return ProfileService.update_project(db, current_user.id, project_id, project_in)


@router.delete("/projects/{project_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_project(
    project_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Delete a project."""
    ProfileService.delete_project(db, current_user.id, project_id)
    return None


# --- Experience Endpoints ---
@router.post("/experience", response_model=ExperienceResponse, status_code=status.HTTP_201_CREATED)
def add_experience(
    exp_in: ExperienceCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Add employment, freelance, or internship experience."""
    return ProfileService.add_experience(db, current_user.id, exp_in)


@router.put("/experience/{exp_id}", response_model=ExperienceResponse)
def update_experience(
    exp_id: str,
    exp_in: ExperienceUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Update experience record."""
    return ProfileService.update_experience(db, current_user.id, exp_id, exp_in)


@router.delete("/experience/{exp_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_experience(
    exp_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Delete experience record."""
    ProfileService.delete_experience(db, current_user.id, exp_id)
    return None


# --- Certifications Endpoints ---
@router.post("/certifications", response_model=CertificationResponse, status_code=status.HTTP_201_CREATED)
def add_certification(
    cert_in: CertificationCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Add an industry certification."""
    return ProfileService.add_certification(db, current_user.id, cert_in)


@router.delete("/certifications/{cert_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_certification(
    cert_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Delete a certification."""
    ProfileService.delete_certification(db, current_user.id, cert_id)
    return None


# --- Social Profiles ---
@router.post("/social-profiles", response_model=SocialProfileResponse, status_code=status.HTTP_201_CREATED)
def add_social_profile(
    social_in: SocialProfileCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Link GitHub, LinkedIn, or portfolio URL."""
    return ProfileService.add_social_profile(db, current_user.id, social_in)


@router.delete("/social-profiles/{social_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_social_profile(
    social_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Unlink social profile."""
    ProfileService.delete_social_profile(db, current_user.id, social_id)
    return None


# --- Job Preferences ---
@router.post("/preferences", response_model=JobPreferenceResponse)
def set_job_preference(
    pref_in: JobPreferenceCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Configure target roles, locations, and salary range."""
    return ProfileService.set_job_preference(db, current_user.id, pref_in)


# --- Profile Photo ---
@router.post("/photo", response_model=PersonalInfoResponse)
async def upload_profile_photo(
    file: UploadFile = File(...),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Upload a profile photo. Validates format (JPEG/PNG), file size (<= 5MB),
    resizes to max 512x512 with safe compression, and saves to storage.
    """
    allowed_types = ["image/jpeg", "image/jpg", "image/png"]
    if file.content_type not in allowed_types:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid image format. Only JPG and PNG are supported."
        )

    # Read binary stream and enforce 5MB limit
    contents = await file.read()
    if len(contents) > 5 * 1024 * 1024:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="File size exceeds the 5MB limit."
        )

    try:
        image = Image.open(io.BytesIO(contents))
        if image.mode in ("RGBA", "P"):
            image = image.convert("RGB")
        image.thumbnail((512, 512))

        storage_dir = os.path.join("storage", "profiles")
        os.makedirs(storage_dir, exist_ok=True)
        file_path = os.path.join(storage_dir, f"{current_user.id}.jpg")
        image.save(file_path, "JPEG", quality=85)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Unable to process image: {str(e)}"
        )

    avatar_url = f"/api/v1/profile/photo/{current_user.id}"
    info = ProfileService.update_personal_info(
        db, current_user.id,
        PersonalInfoCreate(
            full_name=current_user.full_name,
            email=current_user.email,
            avatar_url=avatar_url
        )
    )
    return info


@router.get("/photo/{user_id}")
def get_profile_photo(user_id: str):
    """Serves the uploaded profile photo."""
    file_path = os.path.join("storage", "profiles", f"{user_id}.jpg")
    if not os.path.exists(file_path):
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Profile photo not found.")
    return FileResponse(file_path, media_type="image/jpeg")


@router.delete("/photo", response_model=PersonalInfoResponse)
def delete_profile_photo(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Removes the profile photo."""
    file_path = os.path.join("storage", "profiles", f"{current_user.id}.jpg")
    if os.path.exists(file_path):
        try:
            os.remove(file_path)
        except OSError:
            pass

    info = ProfileService.update_personal_info(
        db, current_user.id,
        PersonalInfoCreate(
            full_name=current_user.full_name,
            email=current_user.email,
            avatar_url=None
        )
    )
    return info

