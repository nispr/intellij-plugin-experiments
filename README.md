# Intellij Plugin Experiments

This is a very simple Kotlin-based Intellij plugin trying out some basic features of the Intellij Plugin SDK.

### Functional features
- An action in the `Generate...` menu lets the user transpile a test case fetched from a specified JIRA/Zephyr API to a Kotlin function

### Technical features
- Persisted project-scoped preferences (credentials, strings)
- Execution of plugin startup logic
- An Action placed in the `Generate...` menu
- Code insertion at current caret position (generated with [KotlinPoet](https://github.com/square/kotlinpoet))
- Formatting current editor content
- Potential i18n
- Dependency injection with [Koin](https://github.com/InsertKoinIO/koin)

### Requirements
- The plugin expects Zephyr API version `TBD`