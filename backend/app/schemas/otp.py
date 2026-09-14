from pydantic import BaseModel, EmailStr


class RegisterStartRequest(BaseModel):
    full_name: str
    email: EmailStr
    password: str


class RegisterStartResponse(BaseModel):
    status: str
    email: str
    message: str
    expires_in_minutes: int = 10


class RegisterVerifyRequest(BaseModel):
    email: EmailStr
    otp: str


class ResendOtpRequest(BaseModel):
    email: EmailStr
