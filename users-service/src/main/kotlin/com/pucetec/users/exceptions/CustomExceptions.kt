package com.pucetec.users.exceptions

class ResourceNotFoundException(message: String) : RuntimeException(message)

class DuplicateResourceException(message: String) : RuntimeException(message)