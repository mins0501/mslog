import HttpRepository from '@/repository/HttpRepository'
import type Login from '@/entity/user/Login'
import { inject, singleton } from 'tsyringe'
import type PostWrite from '@/entity/post/PostWrite'
import { plainToClass, plainToInstance } from 'class-transformer'
import Post from '@/entity/post/Post'
import Paging from '@/entity/data/Paging'
import Signup from "@/views/SignupView.vue";

@singleton()
export default class AuthRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public signup(request: Signup) {
    return this.httpRepository.post({
      path: '/api/auth/signup',
      body: request,
    })
  }

}
