# 📚 Shortenit Phase 2 - Complete API Documentation

**Version:** 2.0  
**Last Updated:** December 11, 2024  
**Base URL:** `http://localhost:8080`

---

## 📋 Table of Contents

1. [Overview](#overview)
2. [Phase 1 Features - URL Shortening](#phase-1-url-shortening)
3. [Phase 2 Features - Analytics](#phase-2-analytics)
4. [Phase 2 Features - Pagination (NEW)](#phase-2-pagination-new)
5. [Authentication](#authentication)
6. [Error Handling](#error-handling)
7. [Rate Limiting](#rate-limiting)
8. [Examples & Use Cases](#examples--use-cases)

---

## 🎯 Overview

Shortenit is a URL shortening service with comprehensive analytics and pagination features.

### **Key Features:**
- ✅ URL shortening with 7-character codes
- ✅ Custom aliases
- ✅ URL expiration
- ✅ Click tracking
- ✅ Geographic analytics (LocalGeoIp)
- ✅ Device/Browser analytics
- ✅ Time-series analytics
- ✅ Pagination with analytics summary (NEW)
- ✅ Sorting and filtering

### **Technology Stack:**
- **Backend:** Spring Boot 3.x
- **Database:** PostgreSQL
- **GeoIP:** MaxMind GeoLite2 (local database)
- **Package:** `edu.au.life.shortenit`

---

## 📌 Phase 1: URL Shortening

### **1.1 Create Short URL**

**Endpoint:** `POST /api/shorten`

**Description:** Create a shortened URL with optional custom alias and expiration.

**Request Body:**
```json
{
  "originalUrl": "https://example.com",          // Required
  "customAlias": "my-link",                      // Optional
  "expirationDays": 30                           // Optional
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "shortCode": "abc1234",
  "originalUrl": "https://example.com",
  "shortUrl": "http://localhost:8080/s/abc1234",
  "customAlias": null,
  "clickCount": 0,
  "createdAt": "2024-12-09T10:30:00",
  "expiresAt": null
}
```

**Validation Rules:**
- `originalUrl`: Must be valid URL format
- `customAlias`: 1-50 characters, alphanumeric and hyphens only
- `expirationDays`: Positive integer

**Error Responses:**
- `400 Bad Request` - Invalid URL format
- `409 Conflict` - Custom alias already exists

---

### **1.2 Get URL Information**

**Endpoint:** `GET /api/urls/{shortCode}`

**Description:** Retrieve information about a shortened URL.

**Path Parameters:**
- `shortCode` (string) - The short code or custom alias

**Response:** `200 OK`
```json
{
  "id": 1,
  "shortCode": "abc1234",
  "originalUrl": "https://example.com",
  "shortUrl": "http://localhost:8080/s/abc1234",
  "customAlias": null,
  "clickCount": 45,
  "createdAt": "2024-12-09T10:30:00",
  "expiresAt": null
}
```

**Error Responses:**
- `404 Not Found` - Short code doesn't exist

---

### **1.3 Redirect to Original URL**

**Endpoint:** `GET /s/{shortCode}`

**Description:** Redirect to the original URL and track the click.

**Path Parameters:**
- `shortCode` (string) - The short code or custom alias

**Response:** `302 Found`
- **Location Header:** Original URL
- Increments click count
- Records analytics (IP, country, city, device, browser, referrer)

**Error Responses:**
- `404 Not Found` - Short code doesn't exist or expired
- `404 Not Found` - URL has been deactivated

**Tracked Data:**
- IP address
- Geographic location (country, city)
- Device type (mobile, desktop, tablet)
- Browser
- Operating system
- Referrer
- Timestamp

---

### **1.4 Delete URL**

**Endpoint:** `DELETE /api/urls/{shortCode}`

**Description:** Permanently delete a shortened URL.

**Path Parameters:**
- `shortCode` (string) - The short code or custom alias

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found` - Short code doesn't exist

---

## 📊 Phase 2: Analytics

### **2.1 Get Complete Analytics**

**Endpoint:** `GET /api/analytics/{shortCode}`

**Description:** Get comprehensive analytics for a shortened URL.

**Path Parameters:**
- `shortCode` (string) - The short code or custom alias

**Response:** `200 OK`
```json
{
  "shortCode": "abc1234",
  "originalUrl": "https://example.com",
  "totalClicks": 150,
  "createdAt": "2024-12-01T10:30:00",
  "lastClickAt": "2024-12-09T14:25:30",
  
  "clicksByCountry": [
    {
      "country": "Thailand",
      "count": 85,
      "percentage": 56.67
    },
    {
      "country": "United States",
      "count": 45,
      "percentage": 30.00
    }
  ],
  
  "clicksByDevice": [
    {
      "deviceType": "mobile",
      "count": 90,
      "percentage": 60.00
    },
    {
      "deviceType": "desktop",
      "count": 50,
      "percentage": 33.33
    },
    {
      "deviceType": "tablet",
      "count": 10,
      "percentage": 6.67
    }
  ],
  
  "clicksByBrowser": [
    {
      "browser": "Chrome",
      "count": 80,
      "percentage": 53.33
    },
    {
      "browser": "Safari",
      "count": 40,
      "percentage": 26.67
    }
  ],
  
  "clicksByReferrer": [
    {
      "referrer": "https://twitter.com",
      "count": 50,
      "percentage": 33.33
    },
    {
      "referrer": "Direct",
      "count": 30,
      "percentage": 20.00
    }
  ],
  
  "clicksOverTime": [
    {
      "date": "2024-12-01",
      "count": 15
    },
    {
      "date": "2024-12-02",
      "count": 23
    }
  ]
}
```

---

### **2.2 Get Analytics by Date Range**

**Endpoint:** `GET /api/analytics/{shortCode}/range`

**Description:** Get analytics filtered by date range.

**Path Parameters:**
- `shortCode` (string) - The short code or custom alias

**Query Parameters:**
- `startDate` (ISO datetime) - Start of date range
- `endDate` (ISO datetime) - End of date range

**Example:**
```
GET /api/analytics/abc1234/range?startDate=2024-12-01T00:00:00&endDate=2024-12-31T23:59:59
```

**Response:** `200 OK`
- Same structure as complete analytics, but filtered by date range

---

## 🔄 Phase 2: Pagination (NEW)

### **3.1 Get All URLs with Pagination**

**Endpoint:** `GET /api/urls`

**Description:** Get paginated list of URLs with analytics summary for each URL.

**Query Parameters:**
- `page` (integer, default: 0) - Page number (0-indexed)
- `size` (integer, default: 10, max: 50) - Items per page
- `sortBy` (string, default: "createdAt") - Field to sort by
- `sortDir` (string, default: "desc") - Sort direction ("asc" or "desc")

**Sortable Fields:**
- `createdAt` - Creation date
- `clickCount` - Number of clicks
- `shortCode` - Short code
- `customAlias` - Custom alias
- `expiresAt` - Expiration date

**Example:**
```
GET /api/urls?page=0&size=10&sortBy=clickCount&sortDir=desc
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "shortCode": "abc1234",
      "originalUrl": "https://example.com",
      "customAlias": null,
      "clickCount": 150,
      "createdAt": "2024-12-01T10:30:00",
      "expiresAt": null,
      "isExpired": false,
      
      "analyticsSummary": {
        "totalClicks": 150,
        "lastClickedAt": "2024-12-09T14:25:30",
        "topCountry": "Thailand",
        "topCountryClicks": 85,
        "topDeviceType": "mobile",
        "topDeviceClicks": 90,
        "clicksToday": 15,
        "clicksThisWeek": 48
      }
    },
    // ... more URLs
  ],
  
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  
  "totalElements": 47,
  "totalPages": 5,
  "last": false,
  "first": true,
  "size": 10,
  "number": 0,
  "numberOfElements": 10,
  "empty": false
}
```

**Analytics Summary Fields:**
- `totalClicks` - Total number of clicks
- `lastClickedAt` - Timestamp of last click (null if no clicks)
- `topCountry` - Country with most clicks (null if no clicks)
- `topCountryClicks` - Number of clicks from top country
- `topDeviceType` - Device type with most clicks (null if no clicks)
- `topDeviceClicks` - Number of clicks from top device
- `clicksToday` - Number of clicks today
- `clicksThisWeek` - Number of clicks in last 7 days

**Pagination Metadata:**
- `totalElements` - Total number of URLs across all pages
- `totalPages` - Total number of pages
- `size` - Items per page (current page)
- `number` - Current page number (0-indexed)
- `numberOfElements` - Number of items in current page
- `first` - Is this the first page?
- `last` - Is this the last page?
- `empty` - Is the page empty?

---

### **3.2 Get Recent URLs**

**Endpoint:** `GET /api/urls/recent`

**Description:** Get most recent URLs sorted by creation date (newest first) with analytics.

**Query Parameters:**
- `page` (integer, default: 0) - Page number
- `size` (integer, default: 10, max: 50) - Items per page

**Example:**
```
GET /api/urls/recent?page=0&size=5
```

**Response:** `200 OK`
- Same structure as paginated URLs response
- Always sorted by `createdAt` descending (newest first)

---

### **3.3 Get All URLs (Legacy - No Pagination)**

**Endpoint:** `GET /api/urls/all`

**Description:** Get all URLs without pagination (backward compatibility).

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "shortCode": "abc1234",
    "originalUrl": "https://example.com",
    "shortUrl": "http://localhost:8080/s/abc1234",
    "customAlias": null,
    "clickCount": 150,
    "createdAt": "2024-12-01T10:30:00",
    "expiresAt": null
  },
  // ... all URLs
]
```

**Note:** This endpoint returns the old UrlResponse format without analytics summary. Use paginated endpoint for analytics.

---

## 🔐 Authentication

**Current Version:** No authentication required (Phase 2)

**Phase 3 (Coming Soon):**
- JWT authentication
- Microsoft OAuth 2.0
- API key support for CLI
- Role-based access control (USER, ADMIN)

---

## ⚠️ Error Handling

### **Error Response Format:**

```json
{
  "status": 404,
  "message": "Short URL not found: invalid999",
  "timestamp": "2024-12-09T10:30:00"
}
```

### **HTTP Status Codes:**

| Code | Meaning | When |
|------|---------|------|
| `200 OK` | Success | GET requests successful |
| `201 Created` | Resource created | URL created successfully |
| `204 No Content` | Success, no body | DELETE successful |
| `400 Bad Request` | Invalid input | Validation failed |
| `404 Not Found` | Resource not found | URL doesn't exist |
| `409 Conflict` | Resource conflict | Custom alias already exists |
| `500 Internal Server Error` | Server error | Unexpected error |

### **Common Error Messages:**

| Error | Message |
|-------|---------|
| Invalid URL | `"must be a valid URL"` |
| Duplicate alias | `"Custom alias already exists: {alias}"` |
| Not found | `"Short URL not found: {code}"` |
| Expired | `"This URL has expired"` |
| Deactivated | `"This URL has been deactivated"` |

---

## ⏱️ Rate Limiting

**Current Version:** No rate limiting (Phase 2)

**Recommendations for Production:**
- 100 requests per minute per IP for URL creation
- 1000 requests per minute for redirects
- 50 requests per minute for analytics

---

## 💡 Examples & Use Cases

### **Example 1: Create and Track Marketing Campaign**

```bash
# 1. Create campaign URL
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://example.com/campaign",
    "customAlias": "summer-sale",
    "expirationDays": 30
  }'

# Response: shortCode = "summer-sale"

# 2. Share URL: http://localhost:8080/s/summer-sale

# 3. View analytics
curl http://localhost:8080/api/analytics/summer-sale

# 4. Check performance in dashboard
curl "http://localhost:8080/api/urls?sortBy=clickCount&sortDir=desc&size=10"
```

---

### **Example 2: Monitor Link Performance**

```bash
# Get top performing links
curl "http://localhost:8080/api/urls?page=0&size=10&sortBy=clickCount&sortDir=desc"

# Get recent activity
curl "http://localhost:8080/api/urls/recent?page=0&size=5"

# Get detailed analytics for specific link
curl http://localhost:8080/api/analytics/abc1234

# Get last week's performance
curl "http://localhost:8080/api/analytics/abc1234/range?startDate=2024-12-02T00:00:00&endDate=2024-12-09T23:59:59"
```

---

### **Example 3: Pagination Navigation**

```bash
# Get first page
curl "http://localhost:8080/api/urls?page=0&size=10"
# Response: totalPages = 5, first = true, last = false

# Get next page
curl "http://localhost:8080/api/urls?page=1&size=10"

# Get last page (if totalPages = 5)
curl "http://localhost:8080/api/urls?page=4&size=10"
# Response: first = false, last = true

# Check if has next page
# If last = false, there's a next page
# If last = true, this is the last page
```

---

### **Example 4: Filter and Sort**

```bash
# Most clicked URLs
curl "http://localhost:8080/api/urls?sortBy=clickCount&sortDir=desc&size=10"

# Oldest URLs first
curl "http://localhost:8080/api/urls?sortBy=createdAt&sortDir=asc&size=10"

# Newest URLs first
curl "http://localhost:8080/api/urls/recent?size=10"

# Get specific page of sorted results
curl "http://localhost:8080/api/urls?page=2&size=20&sortBy=clickCount&sortDir=desc"
```

---

### **Example 5: Analytics Summary vs Full Analytics**

```bash
# Quick overview from list (fast, summary only)
curl "http://localhost:8080/api/urls?page=0&size=10"
# Each URL includes: totalClicks, topCountry, topDevice, clicksToday, clicksThisWeek

# Detailed analytics for specific URL (comprehensive)
curl "http://localhost:8080/api/analytics/abc1234"
# Includes: all countries, all devices, all browsers, time series, referrers
```

**When to use each:**
- **Pagination with summary:** Dashboard overview, monitoring multiple URLs
- **Full analytics:** Deep dive into specific URL performance

---

## 🔧 Configuration

### **Environment Variables:**

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/shortenit
spring.datasource.username=postgres
spring.datasource.password=postgres

# Base URL (important for short URL generation)
app.base-url=http://localhost:8080

# Server
server.port=8080

# Timezone
spring.jackson.time-zone=Asia/Bangkok
```

### **GeoIP Database:**

Location: `/path/to/GeoLite2-City.mmdb`

**Features:**
- Local database (no external API calls)
- Fast lookups (1-5ms)
- No rate limits
- Privacy compliant

---

## 📈 Performance Considerations

### **Response Times:**

| Endpoint | Expected Time |
|----------|--------------|
| POST /api/shorten | < 50ms |
| GET /s/{code} | < 10ms (redirect) |
| GET /api/urls (paginated) | < 200ms |
| GET /api/analytics/{code} | < 300ms |

### **Optimization Tips:**

1. **Pagination:** Always use pagination for large datasets
2. **Analytics Summary:** Use `/api/urls` for quick overview
3. **Full Analytics:** Only fetch when needed for specific URL
4. **Caching:** Consider caching analytics for popular URLs
5. **Indexes:** Database has indexes on shortCode, customAlias, createdAt

---

## 🚀 Best Practices

### **Creating Short URLs:**

✅ **DO:**
- Use custom aliases for marketing campaigns
- Set expiration for time-sensitive content
- Use descriptive aliases (e.g., "summer-sale" not "ss1")

❌ **DON'T:**
- Use sensitive data in custom aliases
- Create very long custom aliases (keep under 30 chars)
- Use confusing characters (l, I, 0, O)

### **Analytics:**

✅ **DO:**
- Check analytics summary in paginated list first
- Use date range filters for specific periods
- Monitor `clicksToday` and `clicksThisWeek` for trending

❌ **DON'T:**
- Fetch full analytics repeatedly (use summary)
- Request large date ranges unnecessarily

### **Pagination:**

✅ **DO:**
- Use reasonable page sizes (10-50)
- Check `totalPages` to know pagination bounds
- Use `first` and `last` flags for navigation
- Sort by relevant field for your use case

❌ **DON'T:**
- Request size > 50 (will be capped)
- Ignore pagination metadata
- Make excessive page requests

---

## 📝 Change Log

### **Version 2.0 (Phase 2 with Pagination)**
- ✅ Added paginated URL listing with analytics summary
- ✅ Added recent URLs endpoint
- ✅ Added sorting capabilities
- ✅ Added analytics summary in list view
- ✅ Kept legacy endpoint for backward compatibility

### **Version 1.1 (Phase 2 - Analytics)**
- ✅ Added comprehensive analytics
- ✅ Added LocalGeoIp service (MaxMind)
- ✅ Added date range filtering
- ✅ Added device/browser tracking

### **Version 1.0 (Phase 1)**
- ✅ Basic URL shortening
- ✅ Custom aliases
- ✅ URL expiration
- ✅ Click tracking
- ✅ Redirect functionality

---

## 🔜 Coming in Phase 3

- JWT authentication
- Microsoft OAuth 2.0 login
- API key management
- CLI program
- Role-based access control
- User-specific URLs
- Admin dashboard

---

## 📞 Support

For issues or questions:
- GitHub: [Your Repository]
- Email: [Your Email]
- Documentation: [Your Docs URL]

---

**Last Updated:** December 11, 2024  
**Version:** 2.0  
**Team:** Monster Trio (2577)  
**Project:** Shortenit - Senior Project 1
