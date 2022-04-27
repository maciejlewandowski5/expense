package com.example.e.domain

interface ResponseModel<G : DomainModel> {
    fun toDomain(): G
}

interface DomainModel
