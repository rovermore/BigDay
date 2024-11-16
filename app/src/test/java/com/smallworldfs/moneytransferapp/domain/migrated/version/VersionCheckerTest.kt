package com.smallworldfs.moneytransferapp.domain.migrated.version

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.version.models.Version
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class VersionCheckerTest {

    private lateinit var versionChecker: VersionChecker

    private val currentAppVersionMock = Version("4.2.1")
    private val higherAppVersionMock = Version("4.4.0")
    private val lowerAppVersionMock = Version("1.10.0")
    private val malformedAppVersionMock = Version("----")

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        versionChecker = VersionChecker()
    }

    @Test
    fun `when calling checkAppVersion, if the versions are equal returns Success(OperationCheck NOTHING)`() {
        val result = versionChecker.checkAppVersion(currentAppVersionMock, currentAppVersionMock)

        Assert.assertEquals(VersionChecker.OperationCheck.NOTHING, result.get())
    }

    @Test
    fun `when calling checkAppVersion, if the required version is lower returns Success(OperationCheck NOTHING)`() {
        val result = versionChecker.checkAppVersion(currentAppVersionMock, lowerAppVersionMock)

        Assert.assertEquals(VersionChecker.OperationCheck.NOTHING, result.get())
    }

    @Test
    fun `when calling checkAppVersion, if the required version is higher returns Success(OperationCheck FORCE)`() {
        val result = versionChecker.checkAppVersion(currentAppVersionMock, higherAppVersionMock)

        Assert.assertEquals(VersionChecker.OperationCheck.FORCE, result.get())
    }

    @Test
    fun `when calling checkAppVersion, if an exception occurs returns Failure(UncompletedOperation)`() {
        val result = versionChecker.checkAppVersion(currentAppVersionMock, malformedAppVersionMock)

        Assert.assertEquals(Error.UncompletedOperation::class, result.get()::class)
    }
}
