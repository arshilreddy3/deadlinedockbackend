# Railway Deployment Guide for DeadlineDock Backend

This Spring Boot application is fully configured for deployment on [Railway](https://railway.app/).

---

## 1. Push Code to GitHub

Open PowerShell in the `DeadlineDock` directory:

```powershell
cd C:\Deadlinedock\deadlinedock-backend
```

If you haven't created a GitHub repository yet:
1. Go to [GitHub](https://github.com/new) and create a new repository named `deadlinedock-backend` (or your preferred name).
2. Run the following commands to push your code:

```powershell
git remote add origin https://github.com/<your-username>/deadlinedock-backend.git
git branch -M main
git push -u origin main
```

*(If you already have a remote repo, simply run `git push`)*

---

## 2. Deploy to Railway

1. Go to [railway.app](https://railway.app/) and sign in.
2. Click **+ New Project** -> **Deploy from GitHub repo**.
3. Select your `deadlinedock-backend` repository.
4. Railway will automatically detect the `Dockerfile` and `railway.json` and start the build.

---

## 3. Configure Environment Variables in Railway

In Railway, navigate to your service -> **Variables** tab, and add the following variables:

| Variable Name | Description | Example Value |
| :--- | :--- | :--- |
| `DB_URL` | JDBC URL of your cloud MySQL instance | `jdbc:mysql://<host>:<port>/<dbname>?useSSL=true&allowPublicKeyRetrieval=true` |
| `DB_USERNAME` | MySQL database username | `root` or `admin` |
| `DB_PASSWORD` | MySQL database password | `your_db_password` |

> [!NOTE]
> `PORT` is assigned automatically by Railway into the container environment. The application is configured to automatically listen on `${PORT:8080}`.

---

## 4. Generate Public Domain & Test

1. In Railway, click **Settings** -> **Networking** -> **Generate Domain**.
2. You will get a public HTTPS URL (e.g., `https://deadlinedock-production.up.railway.app`).
3. Verify your backend is running:
   - Open `https://<your-service>.up.railway.app/api/assignments` in your browser.
   - You should receive an HTTP 200 with an empty JSON array `[]` (or existing records).

---

## 5. Connect Frontend

In your frontend [`script.js`](file:///c:/Deadlinedock/deadlinedock-frontend/script.js#L1):
Change line 1:
```javascript
const API_URL = "https://<your-railway-domain>.up.railway.app/api/assignments";
```
CORS is already configured with `@CrossOrigin(origins = "*")` on `AssignmentController`, so all frontend requests will work seamlessly.
