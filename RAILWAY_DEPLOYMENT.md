# 🚀 Cloud & Production Deployment Guide ("Outside" Deployment)

This Spring Boot application is fully containerized and configured for deployment on cloud platforms such as [Railway](https://railway.app/) and [Render](https://render.com/).

---

## Architecture Overview

```
                      ┌──────────────────────────────────────────┐
                      │          GitHub Pages Frontend           │
                      │  https://arshilreddy3.github.io/deadlinedock/
                      └────────────────────┬─────────────────────┘
                                           │ HTTPS Fetch API
                                           ▼
                      ┌──────────────────────────────────────────┐
                      │          Cloud Backend (Railway)         │
                      │   https://<your-service>.up.railway.app  │
                      └────────────────────┬─────────────────────┘
                                           │ JDBC (Port 3306)
                                           ▼
                      ┌──────────────────────────────────────────┐
                      │          Managed Cloud MySQL             │
                      │      (Railway MySQL / TiDB / Aiven)      │
                      └──────────────────────────────────────────┘
```

---

## Option A: Deploy to Railway (Recommended)

### 1. Push Backend to GitHub
Ensure all latest commits are pushed to your backend repository:
```powershell
cd C:\Deadlinedock\deadlinedock-backend
git add .
git commit -m "ci: add deployment config and health endpoints"
git push origin main
```

### 2. Create Project & MySQL on Railway
1. Go to [railway.app](https://railway.app/) and sign in.
2. Click **+ New Project** -> **Provision MySQL**.
3. Railway automatically sets up a managed MySQL database with credentials (`MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`).

### 3. Deploy Backend Service
1. In the same Railway project, click **+ New** -> **GitHub Repo**.
2. Select your `deadlinedockbackend` repository.
3. Railway automatically detects the `Dockerfile` and builds the production container.

### 4. Connect Backend to MySQL
In your backend service on Railway:
- Go to the **Variables** tab -> click **+ New Variable** -> **Reference**.
- You can either reference Railway MySQL variables or supply a custom `DB_URL`:

| Variable | Recommended Value | Note |
| :--- | :--- | :--- |
| `DB_URL` | `jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false` | Railway variable reference |
| `DB_USERNAME` | `${{MySQL.MYSQLUSER}}` | MySQL username |
| `DB_PASSWORD` | `${{MySQL.MYSQLPASSWORD}}` | MySQL password |

> [!NOTE]
> `PORT` is assigned automatically by Railway into the container environment. The application is configured to automatically listen on `${PORT:8080}`.

### 5. Generate Public Domain & Test
1. In Railway, click **Settings** -> **Networking** -> **Generate Domain**.
2. You will receive a public HTTPS URL (e.g. `https://deadlinedockbackend-production.up.railway.app`).
3. Verify your backend is running:
   - Root health check: `https://<your-service>.up.railway.app/` -> Returns `{"status":"UP","app":"DeadlineDock Backend"}`
   - Health check: `https://<your-service>.up.railway.app/api/health` -> Returns `{"status":"UP"}`
   - Assignments: `https://<your-service>.up.railway.app/api/assignments` -> Returns `[]`

---

## Option B: Deploy to Render (Alternative Free Tier)

1. Sign up at [render.com](https://render.com/).
2. Click **New +** -> **Web Service**.
3. Connect your GitHub repository `arshilreddy3/deadlinedockbackend`.
4. Runtime: **Docker**.
5. Environment Variables:
   - `DB_URL`: `jdbc:mysql://<host>:<port>/<dbname>?useSSL=true&allowPublicKeyRetrieval=true`
   - `DB_USERNAME`: `<db_user>`
   - `DB_PASSWORD`: `<db_password>`
6. Render will build and deploy the container at `https://deadlinedockbackend.onrender.com`.

---

## 6. Connect Frontend to the Cloud Backend

Once you have your cloud backend URL (e.g., `https://deadlinedockbackend-production.up.railway.app`):

1. Open [`c:/Deadlinedock/deadlinedock-frontend/script.js`](file:///c:/Deadlinedock/deadlinedock-frontend/script.js#L6)
2. Update `PRODUCTION_API_URL`:
   ```javascript
   const PRODUCTION_API_URL = "https://<your-service>.up.railway.app/api/assignments";
   ```
3. Commit and push:
   ```powershell
   cd C:\Deadlinedock\deadlinedock-frontend
   git add script.js
   git commit -m "chore: connect frontend to cloud backend URL"
   git push origin main
   ```
4. GitHub Pages will instantly update, and your live application will now communicate directly with your live cloud backend!
