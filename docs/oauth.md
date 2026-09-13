# 🔐 JobPilot Third-Party Integrations & OAuth 2.0 Setup Guide

This guide describes how to configure Google/Gmail, GitHub, and LinkedIn integrations for JobPilot.

---

## 1. Google & Gmail OAuth 2.0 Integration

JobPilot connects with Google OAuth 2.0 to scan job application confirmations and recruiter replies.

> [!IMPORTANT]
> JobPilot **never** asks for or stores candidate Gmail passwords. It uses official scoped OAuth 2.0 access and refresh tokens.

### Step-by-Step Setup:
1. Navigate to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new Project named **JobPilot**.
3. Under **APIs & Services > Library**, enable the **Gmail API**.
4. Configure the **OAuth Consent Screen**:
   - User Type: External (or Internal for Google Workspace).
   - App name: `JobPilot`.
   - Developer email: Your email.
   - Scopes: `https://www.googleapis.com/auth/gmail.readonly`, `email`, `profile`.
5. Under **Credentials**, click **Create Credentials > OAuth Client ID**:
   - Application Type: **Web application**.
   - Authorized redirect URIs:
     `http://localhost:8000/api/v1/integrations/google/callback`
6. Copy the **Client ID** and **Client Secret** into `backend/.env`:
   ```env
   GOOGLE_CLIENT_ID=your-google-client-id.apps.googleusercontent.com
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   GOOGLE_REDIRECT_URI=http://localhost:8000/api/v1/integrations/google/callback
   ```

---

## 2. GitHub Integration

JobPilot allows candidates to import repositories directly into their Career Profile projects.

### Step-by-Step Setup:
1. Log in to [GitHub](https://github.com/) and go to **Settings > Developer Settings > OAuth Apps**.
2. Click **New OAuth App**:
   - Application Name: `JobPilot Career Hub`
   - Homepage URL: `http://localhost:8000`
   - Authorization callback URL:
     `http://localhost:8000/api/v1/integrations/github/callback`
3. Click **Register Application**.
4. Generate a **Client Secret**.
5. Add credentials to `backend/.env`:
   ```env
   GITHUB_CLIENT_ID=your-github-client-id
   GITHUB_CLIENT_SECRET=your-github-client-secret
   GITHUB_REDIRECT_URI=http://localhost:8000/api/v1/integrations/github/callback
   ```

---

## 3. LinkedIn Official Integration Architecture

JobPilot integrates with LinkedIn via official REST endpoints:
- **Sign In with LinkedIn (OpenID Connect)** for identity and profile information.
- Direct sync of public profile URLs into candidate contact details.

### Step-by-Step Setup:
1. Log in to the [LinkedIn Developer Portal](https://www.linkedin.com/developers/).
2. Click **Create App**:
   - App Name: `JobPilot`
   - Associated LinkedIn Page.
3. Under **Products**, add:
   - **Sign In with LinkedIn using OpenID Connect**
   - **Share on LinkedIn** (optional)
4. Under **Auth**, configure the Redirect URL:
   `http://localhost:8000/api/v1/integrations/linkedin/callback`
5. Copy credentials into `backend/.env`:
   ```env
   LINKEDIN_CLIENT_ID=your-linkedin-client-id
   LINKEDIN_CLIENT_SECRET=your-linkedin-client-secret
   LINKEDIN_REDIRECT_URI=http://localhost:8000/api/v1/integrations/linkedin/callback
   ```
