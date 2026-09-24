const BASE_URL = 'http://localhost:8080/api/costcompass'

const fetchApi = (path: string, options?: RequestInit) => {
  return fetch(`${BASE_URL}/${path}`, options)
}

export async function apiGet<T>(path: string): Promise<T> {
  const res = await fetchApi(path)
  return res.json()
}

export async function apiDelete(path: string): Promise<void> {
  await fetchApi(path, {method: 'DELETE'})
}

export async function apiPost(path: string): Promise<void> {
  await fetchApi(path, {method: 'POST'})
}
