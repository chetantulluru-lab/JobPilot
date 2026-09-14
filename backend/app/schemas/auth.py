from datetime import datetime
from typing import Optional
from pydantic import BaseModel, EmailStr, Field, ConfigDict


class UserRegister(BaseModel):
    email: EmailStr
    password: str = Field(..., min_length=6, description="Password must be at least 6 characters")
    full_name: str = Field(..., min_length=2, max_length=100)
    keystone: Optional[str] = Field(default="jobpilot", min_length=3, max_length=100, description="Secret security word used to reset password")


class UserLogin(BaseModel):
    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str = "bearer"
    expires_in: int


class TokenRefresh(BaseModel):
    refresh_token: str


class UserResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    email: str
    full_name: str
    is_active: bool
    is_verified: bool
    created_at: datetime


class ForgotPasswordRequest(BaseModel):
    email: EmailStr


class ResetPasswordWithKeystoneRequest(BaseModel):
    email: EmailStr
    keystone: str = Field(..., min_length=3, max_length=100)
    new_password: str = Field(..., min_length=6, description="New password must be at least 6 characters")


class MessageResponse(BaseModel):
    status: str = "success"
    message: str
