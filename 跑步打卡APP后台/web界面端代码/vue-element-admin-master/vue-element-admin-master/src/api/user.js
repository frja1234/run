import request from '@/utils/request'

export function login (data) {
  // console.log(data, 'data')
  const res = request({
    /* url: '/vue-element-admin/user/login',
    method: 'post',
    data */
    url: '/api/tb-user/login',
    method: 'get',
    params: {
      userNum: data.username,
      userPassword: data.password
    }
  })
  return res
}

export function getInfo (userId) {
  // console.log(token, 'getInfo token')
  return request({
    url: '/api/tb-user/info',
    method: 'get',
    params: {
      userId: userId
    }
  })
}

export function logout () {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}
