from app.schemas.auth import (
    UserRegister,
    UserLogin,
    TokenResponse,
    TokenRefresh,
    UserResponse,
    ForgotPasswordRequest,
    MessageResponse
)
from app.schemas.profile import (
    PersonalInfoCreate,
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
    JobPreferenceResponse,
    CareerProfileResponse,
    CareerProfileUpdate
)
from app.schemas.resume import ResumeResponse, MissingFieldsAuditResponse
from app.schemas.job import JobCreate, JobResponse, JobMatchResponse, SkillGapResponse, SkillGapItem
from app.schemas.application import (
    ApplicationCreate,
    ApplicationUpdate,
    ApplicationResponse,
    ApplicationEventCreate,
    ApplicationEventResponse
)
from app.schemas.notification import NotificationResponse, NotificationUpdateRead
from app.schemas.integration import (
    ConnectedAccountResponse,
    EmailEventResponse,
    EmailClassifyRequest,
    EmailClassifyResponse
)
