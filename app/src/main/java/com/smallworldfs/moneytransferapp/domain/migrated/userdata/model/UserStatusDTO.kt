package com.smallworldfs.moneytransferapp.domain.migrated.userdata.model

sealed class UserStatusDTO(val status: String) {
    class SmsPending(status: String) : UserStatusDTO(status)
    class NoClientId(status: String) : UserStatusDTO(status)
    class ApprovedEmailPending(status: String) : UserStatusDTO(status)
    class ProfilePending(status: String) : UserStatusDTO(status)
    class Approved(status: String) : UserStatusDTO(status)
    class Unknown(status: String) : UserStatusDTO(status)
}
