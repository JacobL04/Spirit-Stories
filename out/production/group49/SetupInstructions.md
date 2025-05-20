# Setting Up SSH Access for GitLab Repository

This guide will walk you through setting up SSH access to your GitLab repository.

## Step 1: Generate an ED25519 SSH Key

First, you need to generate an ED25519 SSH key. Follow the instructions provided in [this video](https://www.youtube.com/watch?v=3PHfKFhvldc).

Alternatively, you can generate the key manually using the following command:

```bash
ssh-keygen -t ed25519 -C "your-email@example.com"
```

- When prompted, specify a file to save the key (or press Enter to accept the default location, typically `~/.ssh/id_ed25519`).
- Set a passphrase for added security or press Enter to skip.

Once completed, your public key will be saved in a file ending with `.pub` (e.g., `id_ed25519.pub`).

## Step 2: Add Your SSH Key to GitLab

1. Log in to your GitLab account.
2. Navigate to your repository.
3. At the top of the page, you should see a warning prompting you to add an SSH key.
4. Open your public key file (e.g., `~/.ssh/id_ed25519.pub`) with a text editor:

   ```bash
   cat ~/.ssh/id_ed25519.pub
   ```

5. Copy the entire contents of the public key.
6. Paste the copied key into the GitLab SSH key section.
7. Click **Add Key** to save it.

## Step 3: Clone the Repository Using SSH

Once your SSH key is added, you can clone the repository using SSH with the following steps:

1. Copy the SSH clone URL from GitLab (it will look like `ssh://git@gitlab.com:username/repository.git `).
2. Open a terminal and run:

   ```bash
   git clone ssh://git@gitlab.sci.uwo.ca:7999/courses/2025/01/COMPSCI2212/group49.git
   ```

3. If this is your first time connecting, you may be prompted to confirm the authenticity of the GitLab server. Type `yes` and press Enter.

Your repository should now be cloned successfully.

---

Now you're ready to work with your GitLab repository over SSH! Let's have fun!

