function c(t){const e=document.cookie,o=e.search(t);if(o===-1)return null;let n=e.indexOf(";",o);return n=n===-1?e.length:n,e.slice(o+t.length+1,n)}export{c as g};
