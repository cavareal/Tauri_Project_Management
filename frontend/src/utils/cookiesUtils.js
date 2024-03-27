export default function getCookie(name) {
	let cookies = document.cookie
	let index = cookies.search(name)
	if (index === -1) {
		return "World !"
	} else {
		let end = cookies.indexOf(";", index)
		end = end === -1 ? cookies.length : end
		return cookies.slice(index + name.length + 1, end)
	}
}