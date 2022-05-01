# Intellij Plugin Experiments

This is a very simple Intellij plugin trying out some basic features of the Intellij Plugin SDK.

### Functional features
- Transpiling test cases fetched from a specified JIRA/Zephyr API to Kotlin code.

### Technical features
- Persisted project-scoped preferences (credentials, strings)
- Execution of plugin startup logic
- Action placed in the `Generate...` menu
- Code insertion at current caret position (generated with [KotlinPoet](https://github.com/square/kotlinpoet))
- Formatting current editor content
- Potential i18n

### Requirements
- The plugin expects Zephyr API version `TBD`