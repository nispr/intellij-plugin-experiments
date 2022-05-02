package com.nispr.intellij.zephyr.settings

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe

interface CredentialRepository {
    fun get(): Credentials?
    fun set(userName: String, password: String)
}

class CredentialRepositoryImpl: CredentialRepository {

    override fun set(userName: String, password: String) {
        val credentials = Credentials(userName, password)
        PasswordSafe.instance.set(createCredentialAttributes(), credentials)
    }

    override fun get(): Credentials? = PasswordSafe.instance.get(createCredentialAttributes())

    private fun createCredentialAttributes() = CredentialAttributes(
        generateServiceName("Zephyr", KEY_JIRA_CREDENTIALS)
    )

    companion object {
        private const val KEY_JIRA_CREDENTIALS = "JIRA_CREDENTIALS"
    }
}