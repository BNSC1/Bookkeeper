package com.example.bookkeeper.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ebner.roomdatabasebackup.core.RoomBackup
import com.example.bookkeeper.R
import com.example.bookkeeper.model.EntryDB
import com.example.bookkeeper.model.EntryVM


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var entryVM: EntryVM
        private lateinit var database: EntryDB
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            entryVM = ViewModelProvider(this).get(EntryVM::class.java)
            database = EntryDB.getDB(requireContext())
            // Backup Button
            val backupBTN: Preference? = preferenceManager.findPreference("backup")
            if (backupBTN != null) {
                backupBTN.onPreferenceClickListener =
                    Preference.OnPreferenceClickListener {
                        RoomBackup()
                            .context(this.requireContext())
                            .database(database)
                            .enableLogDebug(true)
                            .backupIsEncrypted(false)
                            .useExternalStorage(false)
                            .apply {
                                onCompleteListener { success, _ ->
                                    if (success) {
                                        Toast.makeText(activity,
                                            getString(R.string.msg_backup_success),
                                            Toast.LENGTH_SHORT).show()
                                        restartApp(Intent(context, MainActivity::class.java))
                                    }
                                }
                            }
                            .backup()
                        true
                    }
            }
            //Restore button
            val restoreBTN: Preference? = preferenceManager.findPreference("restore")
            if (restoreBTN != null) {
                restoreBTN.onPreferenceClickListener =
                    Preference.OnPreferenceClickListener {
                        RoomBackup()
                            .context(requireContext())
                            .database(database)
                            .enableLogDebug(true)
                            .backupIsEncrypted(false)
                            .useExternalStorage(false)
                            .apply {
                                onCompleteListener { success, _ ->
                                    if (success) {
                                        Toast.makeText(activity,
                                            getString(R.string.msg_restore_success),
                                            Toast.LENGTH_SHORT).show()
                                        restartApp(Intent(context, MainActivity::class.java))
                                    }
                                }

                            }
                            .restore()
                        true
                    }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}