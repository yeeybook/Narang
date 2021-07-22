import { createRouter, createWebHistory } from 'vue-router'
import home from '@/views/home/home'
import waitingRoom from '@/views/waitingRoom/waitingRoom'
import gameRoom from '@/views/gameRoom/gameRoom'
import Mypage from '@/views/mypage/mypage'


function makeRoutesFromMenu () {
  let routes = []
  // menu 자체에는 나오지 않는 페이지 라우터에 추가(방 상세보기)
  routes.push(
  {
    path: '/',
    name: 'home',
    component: home
  },
  {
    path: '/waiting-room',
    name: 'waitingRoom',
    component: waitingRoom
  },
  {
    path: '/mypage/:userId',
    name: 'mypage',
    component: Mypage
  },
  {
    path: '/game-room/:roomId',
    name: 'gameRoom',
    component: gameRoom
  },
  )
  return routes
}

const routes = makeRoutesFromMenu()

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.afterEach((to) => {
  console.log(to)
})

export default router
