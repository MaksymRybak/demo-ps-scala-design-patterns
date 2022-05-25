package com.github.maksymrybak.scaladesignpatterns.dao

import com.github.maksymrybak.scaladesignpatterns.model.Model.User

trait UserDaoComponent {

  /** Expose user DAO as a member */
  val userDao: Dao[User]


}
