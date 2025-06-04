# Linear IntelliJ Plugin

[![Latest Release](https://img.shields.io/github/v/release/rsheasby/Intellij-Linear?style=for-the-badge&logo=github)](https://github.com/rsheasby/Intellij-Linear/releases/latest)
[![Download](https://img.shields.io/badge/Download-Plugin-blue?style=for-the-badge&logo=intellij-idea)](https://github.com/rsheasby/Intellij-Linear/releases/latest/download/linear-intellij-plugin-1.0.1.zip)

An IntelliJ IDEA plugin that integrates Linear project management directly into your IDE. View and manage your Linear tasks from a convenient sidebar without leaving your development environment.

## Features

- View assigned Linear tasks in a dedicated tool window
- See task details including title, description, state, priority, and labels
- Quick link to open tasks in Linear web app
- Refresh tasks with a single click
- Secure API key storage in IntelliJ settings

## Installation

### Option 1: Download from Releases (Recommended)

1. **[Download the latest release](https://github.com/rsheasby/Intellij-Linear/releases/latest)** from GitHub
2. Open IntelliJ IDEA
3. Go to **File → Settings → Plugins** (or **IntelliJ IDEA → Preferences → Plugins** on macOS)
4. Click the gear icon ⚙️ and select **Install Plugin from Disk...**
5. Select the downloaded `.zip` file
6. Restart IntelliJ IDEA

### Option 2: Build from Source

1. Build the plugin:
   ```bash
   cd linear-intellij-plugin
   ./gradlew buildPlugin
   ```

2. Install the plugin in IntelliJ IDEA:
   - Go to Settings/Preferences → Plugins → ⚙️ → Install Plugin from Disk
   - Select the generated `.zip` file from `build/distributions/`
   - Restart IntelliJ IDEA

## Configuration

1. Get your Linear API key:
   - Go to Linear Settings → API → Personal API keys
   - Create a new key with 'read' scope
   - Copy the generated key

2. Configure the plugin:
   - In IntelliJ IDEA, go to Settings/Preferences → Tools → Linear
   - Paste your API key
   - Click Apply

## Usage

1. Open the Linear Tasks tool window from the right sidebar
2. Your assigned tasks will automatically load
3. Click on a task to see its details
4. Use the refresh button to update the task list
5. Click "View in Linear" to open the task in your browser

## Development

### Prerequisites
- IntelliJ IDEA 2023.3 or later
- JDK 17 or later
- Gradle 8.0 or later

### Building from source
```bash
git clone <repository>
cd linear-intellij-plugin
./gradlew buildPlugin
```

### Running in development
```bash
./gradlew runIde
```

## License

This project is licensed under the MIT License.