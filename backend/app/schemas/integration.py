from datetime import datetime
from typing import Optional, List, Dict, Any
from pydantic import BaseModel, ConfigDict


class ConnectedAccountResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    user_id: str
    provider: str
    account_email: Optional[str] = None
    account_name: Optional[str] = None
    avatar_url: Optional[str] = None
    is_connected: bool
    scopes: Optional[str] = None
    last_synced_at: Optional[datetime] = None
    created_at: datetime


class EmailEventResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    sender: str
    company: str
    subject: str
    snippet: str
    category: str
    detected_date: str
    is_processed: bool
    created_at: datetime


class EmailClassifyRequest(BaseModel):
    sender: str
    subject: str
    body: str


class EmailClassifyResponse(BaseModel):
    category: str
    confidence: float
    explanation: str
    suggested_action: str


class GitHubRepoDto(BaseModel):
    name: str
    full_name: str
    html_url: str
    description: Optional[str] = None
    language: Optional[str] = None
    stargazers_count: int = 0
    updated_at: Optional[str] = None


class GitHubImportRequest(BaseModel):
    repo_name: str
    title: Optional[str] = None
    description: Optional[str] = None
    tech_stack: Optional[str] = None
    github_url: Optional[str] = None


class ProviderStatus(BaseModel):
    provider: str
    is_configured: bool
    is_connected: bool
    account_email: Optional[str] = None
    account_name: Optional[str] = None
    last_synced_at: Optional[datetime] = None
    documentation_note: str


class IntegrationsOverviewResponse(BaseModel):
    google: ProviderStatus
    github: ProviderStatus
    linkedin: ProviderStatus
