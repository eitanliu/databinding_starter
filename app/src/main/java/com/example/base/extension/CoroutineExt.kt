package com.example.base.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val IOCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)