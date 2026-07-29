package com.pucetec.geomed.exceptions

class ResourceNotFoundException(message: String) : RuntimeException(message)

class DuplicateResourceException(message: String) : RuntimeException(message)

class InvalidStatusException(message: String) : RuntimeException(message)
