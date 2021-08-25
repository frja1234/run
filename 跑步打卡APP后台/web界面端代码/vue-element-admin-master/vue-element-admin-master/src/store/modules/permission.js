import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission (roles, route) {
  if (route.meta && route.meta.roles) {
    // console.log(route.meta.roles, 'route.meta.roles')
    // return roles.some(role => route.meta.roles.includes(role.token))
    return route.meta.roles.includes(roles.token)
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param role
 */
export function filterAsyncRoutes (routes, role) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(role, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, role)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    // console.log(routes, 'routes') /404
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
    // console.log(state.routes, 'state.routes') array[11]
  }
}

const actions = {
  generateRoutes ({ commit }, role) {
    return new Promise(resolve => {
      let accessedRoutes
      if (role.token === 'admin') {
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, role)
      }
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
