# Setting Up GitHub Repository

## Prerequisites

1. **Install Git for Windows** (if not already installed):
   - Download from: https://git-scm.com/download/win
   - Or use winget: `winget install --id Git.Git -e --source winget`
   - Or use chocolatey: `choco install git`

2. **Create a GitHub Account** (if you don't have one):
   - Go to: https://github.com
   - Sign up for a free account

3. **Set up Git** (first time only):
   ```bash
   git config --global user.name "Your Name"
   git config --global user.email "your.email@example.com"
   ```

## Steps to Push to GitHub

### Step 1: Initialize Git Repository
```bash
cd "C:\tasty town"
git init
```

### Step 2: Add All Files
```bash
git add .
```

### Step 3: Create Initial Commit
```bash
git commit -m "Initial commit: Tasty Town project with backend and frontend"
```

### Step 4: Create GitHub Repository
1. Go to https://github.com/new
2. Repository name: `tasty-town` (or your preferred name)
3. Choose Public or Private
4. **DO NOT** initialize with README, .gitignore, or license (we already have files)
5. Click "Create repository"

### Step 5: Connect and Push
After creating the repository, GitHub will show you commands. Use these:

```bash
git remote add origin https://github.com/YOUR_USERNAME/tasty-town.git
git branch -M main
git push -u origin main
```

Replace `YOUR_USERNAME` with your actual GitHub username and `tasty-town` with your repository name.

## Alternative: Using GitHub CLI

If you have GitHub CLI installed:
```bash
gh auth login
gh repo create tasty-town --public --source=. --remote=origin --push
```

## Project Structure

- `tasty town - backend/` - Spring Boot backend application
- `tasty town - frontend/` - React frontend application

Both directories have their own `.gitignore` files, and there's also a root-level `.gitignore` for common files.

