import requests

# Base URL of the target application
base_url = 'http://localhost:8882/api/'

# XSS payload for testing
xss_payload = '<script>alert("XSS test")</script>'

# Endpoints to test with corresponding fields
endpoints = [
    {'endpoint': 'students', 'fields': {'name': xss_payload, 'description': xss_payload}},
    {'endpoint': 'students/1', 'fields': {}},
    {'endpoint': 'uploadCSV', 'fields': {'file-upload': xss_payload, 'projectId': 1}},
    {'endpoint': 'students/1/bonus', 'fields': {'limited': 'true', 'sprintId': '1'}},
    {'endpoint': 'students/1/gradeType/1/author/1', 'fields': {'sprintId': '1'}}
]

# Testing for XSS vulnerabilities on each endpoint
for endpoint_info in endpoints:
    url = base_url + endpoint_info['endpoint']
    data = endpoint_info['fields']
    response = requests.post(url, data=data)
    if response.status_code == 200:
        print(f"XSS vulnerability detected on endpoint: {endpoint_info['endpoint']}")
    else:
        print(f"No XSS vulnerability detected on endpoint: {endpoint_info['endpoint']}")
