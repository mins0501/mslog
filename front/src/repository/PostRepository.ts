import HttpRepository from '@/repository/HttpRepository'
import type Login from '@/entity/user/Login'
import { inject, singleton } from 'tsyringe'
import type PostWrite from '@/entity/post/PostWrite'
import { plainToClass, plainToInstance } from 'class-transformer'
import Post from '@/entity/post/Post'
import Paging from '@/entity/data/Paging'

@singleton()
export default class PostRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: PostWrite) {
    return this.httpRepository.post({
      path: '/api/post',
      body: request,
    })
  }

  public get(postId: number) {
    return this.httpRepository.get<Post>({ path: `/api/post/${postId}` }, Post)
  }

  public getList(page: number) {
    return this.httpRepository.getList<Post>(
      {
        path: `/api/post?page=${page}&size=5`,
      },
      Post
    )
  }

  public delete(postId: number) {
    return this.httpRepository.delete({
      path: `/api/post/${postId}`,
    })
  }
}
