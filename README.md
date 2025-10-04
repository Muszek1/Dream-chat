# Dream-Chat

Dream-Chat is a Bukkit/Spigot plugin for advanced chat moderation and management, including bans, mutes, warnings, and Discord webhook integration.

## Features

- Ban, mute, warn, and kick players
- Temporary bans and mutes
- Silent moderation commands
- Multi-account detection
- Discord webhook notifications
- Configurable messages and storage

## Installation

1. Download the latest `Dream-Chat` jar from the [releases](https://github.com/Muszek1/Dream-chat/releases).
2. Place the jar file in your server's `plugins` directory.
3. Restart your server.

## Configuration

- Edit `config.yml` and `messages.yml` in the `Dream-Chat` plugin folder to customize settings and messages.
- Set up your Discord webhook URL in the configuration to enable notifications.

## Commands

- `/ban <player> <reason>`
- `/unban <player>`
- `/mute <player> <reason>`
- `/unmute <player>`
- `/kick <player> <reason>`
- `/warn <player> <reason>`
- `/tempban <player> <duration> <reason>`
- `/tempmute <player> <duration> <reason>`
- `/banip <ip>`
- `/unbanip <ip>`
- `/checkban <player>`

## Permissions

Each command has its own permission node. See the plugin documentation for details.

## Support

For issues or feature requests, open an issue on [GitHub](https://github.com/Muszek1/Dream-chat/issues).

## License

This project is licensed under the MIT License.
