// API
import $axios from 'axios'

// 로그인
export function requestLogin({ state }, payload) {
  console.log('requestLogin', state, payload)
  const url = '/api/v1/auth/login'
  let body = payload
  return $axios.post(url, body)
}

// 로그아웃
export function requestLogout({ state }) {
  console.log('requestLogout', state)
  localStorage.removeItem('access_token')
  localStorage.removeItem('email')
  localStorage.removeItem('username')
  localStorage.removeItem('profileImageURL')
  return
}

// 회원가입
export function requestSignup({ state }, payload) {
  console.log('requestSignup', state, payload)
  const url = '/api/v1/user'
  let body = payload
  return $axios.post(url, body)
}

// 유저 정보 : 이메일 중복 체크
export function requestEmailCheck({ state }, payload) {
  console.log('requestEmailCheck', state, payload)
  const url = `/api/v1/user/chkemail/${payload.email}`
  return $axios.get(url)
}

// 유저 정보 : 이름 중복 체크
export function requestNameCheck({ state }, payload) {
  console.log('requestNameCheck', state, payload)
  const url = `/api/v1/user/chkusername/${payload.username}`
  return $axios.get(url)
}

// 내 프로필
export function requestReadMyInfo({ state }) {
  console.log('requestMyInfo', state)
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''} // 토큰
  const url = '/api/v1/user'
  return $axios.get(url, {headers: headers})
}

// 유저 정보 수정
export function requestUpdateMyInfo({ state }, payload) {
  console.log('requestMyInfo', state, payload)
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''} // 토큰
  const url = '/api/v1/user'
  let body = payload
  console.log(body)
  return $axios.patch(url, body, {headers: headers})
}

// 유저 정보 삭제
export function requestDeleteMyInfo({ state }) {
  console.log('requestMyInfo', state)
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''} // 토큰
  const url = '/api/v1/user'
  return $axios.delete(url, {headers: headers})
}

// 유저 프로필 삭제
export function requestDeleteMyProfile({ state }) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''} // 토큰
  const url = '/api/v1/user/profile'
  return $axios.delete(url, { headers: headers })
}

// 유저 비밀번호 업데이트
export function requestChangeMyPassword({ state }, payload) {
  const headers = {
    'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : '',
    'Content-Type': 'multipart/form-data'
  }
  const url = '/api/v1/user'
  const body = payload
  return $axios.patch(url, body, {headers: headers})
}

// 게임룸 조회
export function requestReadGameRoomList({ state }, payload) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  let url = '/api/v1/room'
  const params = payload
  return $axios.get(url, {
    headers: headers,
    params: params,
  })
}

// 게임룸 생성
export function requestCreateGameRoom({ state }, payload) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  const url = '/api/v1/room'
  const body = payload
  return $axios.post(url, body, { headers: headers })
}

// 게임룸 입장
export function requestEnterGameRoom({ state }, payload) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  const url = `/api/v1/room/${payload.roomId}`
  const body = { password: payload.password }
  return $axios.post(url, body, {headers: headers})
}

// 게임룸 퇴장
export function requestLeaveGameRoom({ state }, payload) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  const url = `/api/v1/room/${payload.roomId}`
  console.log(payload.roomId)
  return $axios.delete(url, {headers: headers})
}

// 게임 룸 내에서 설정 변경
export function requestUpdateGameRoom({ state }, payload) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  const url = `/api/v1/room/${payload.roomId}`
  const body = {
    ...payload
  }
  console.log(body)
  return $axios.patch(url, body, { headers: headers })
}

export function requestReadSingleGameRoom({ state }, roomId) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  const url = `/api/v1/room/${roomId}`
  return $axios.get(url, { headers: headers })
}

// 게임에 참여하고 있는 유저 정보 가져오기
export function requestReadUserList({ state }, roomId) {
  const headers = {'Authorization': state.accessToken ? `Bearer ${state.accessToken}` : ''}
  const url = `/api/v1/room/userlist/${roomId}`
  return $axios.get(url, { headers: headers })
}
