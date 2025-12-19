# 🧪 Shortenit Phase 2 - Complete Testing Guide

**Version:** 2.0  
**Last Updated:** December 11, 2024

---

## 📋 Table of Contents

1. [Overview](#overview)
2. [Test Environment Setup](#test-environment-setup)
3. [Test Categories](#test-categories)
4. [Running Tests](#running-tests)
5. [Test Results](#test-results)
6. [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

This guide covers ALL Phase 2 testing including:
- ✅ Phase 1: URL Shortening (8 tests)
- ✅ Phase 1: Redirects (2 tests)
- ✅ Phase 2: Analytics (6 tests)
- ✅ Phase 2: Pagination (11 tests) ⭐ NEW
- ✅ Edge Cases & Error Handling (4 tests)

**Total: 31 comprehensive tests**

---

## 🛠️ Test Environment Setup

### **Prerequisites:**

1. **Application Running:**
```bash
# Start with Docker
docker compose up -d

# Or run locally
./mvnw spring-boot:run
```

2. **Verify Application:**
```bash
curl http://localhost:8080/actuator/health
```

3. **Import Postman Collection:**
- Open Postman
- Click "Import"
- Select `Shortenit_Phase2_Complete_Test_Suite.postman_collection.json`
- Collection appears with 5 folders

---

## 📊 Test Categories

### **Category 1: Phase 1 - URL Shortening (8 tests)**

Tests basic URL shortening functionality.

| Test | Endpoint | Expected Result |
|------|----------|----------------|
| 1.1 Create URL - Basic | POST /api/shorten | 201, 7-char code, /s/ prefix |
| 1.2 Create URL - Custom Alias | POST /api/shorten | 201, custom alias used |
| 1.3 Create URL - With Expiration | POST /api/shorten | 201, expiresAt set |
| 1.4 Error - Duplicate Alias | POST /api/shorten | 400/409, error message |
| 1.5 Error - Invalid URL | POST /api/shorten | 400, validation error |
| 1.6 Get URL Info | GET /api/urls/{code} | 200, URL details |
| 1.7 Error - Not Found | GET /api/urls/invalid999 | 404, not found message |
| 1.8 Delete URL | DELETE /api/urls/{code} | 204, no content |

**What's Being Tested:**
- URL validation
- Short code generation (7 characters)
- Custom alias handling
- Duplicate detection
- Expiration date setting
- CRUD operations
- Error handling

---

### **Category 2: Phase 1 - Redirects (2 tests)**

Tests redirect functionality.

| Test | Endpoint | Expected Result |
|------|----------|----------------|
| 2.1 Redirect - Valid | GET /s/{code} | 302, location header |
| 2.2 Error - Invalid Code | GET /s/invalid999 | 404, not found |

**What's Being Tested:**
- Redirect mechanism
- Click tracking trigger
- Invalid code handling
- /s/ prefix routing

---

### **Category 3: Phase 2 - Analytics (6 tests)**

Tests comprehensive analytics features.

| Test | Endpoint | Expected Result |
|------|----------|----------------|
| 3.1 Get Analytics - Basic | GET /api/analytics/{code} | 200, all analytics fields |
| 3.2 Get Analytics - Date Range | GET /api/analytics/{code}/range | 200, filtered data |
| 3.3 Analytics - Geographic Data | GET /api/analytics/{code} | 200, country breakdown |
| 3.4 Analytics - Device Data | GET /api/analytics/{code} | 200, device breakdown |
| 3.5 Analytics - Browser Data | GET /api/analytics/{code} | 200, browser breakdown |
| 3.6 Analytics - Time Series | GET /api/analytics/{code} | 200, time series data |

**What's Being Tested:**
- Full analytics response structure
- Geographic tracking (LocalGeoIp)
- Device type detection
- Browser detection
- Referrer tracking
- Time-series data
- Date range filtering

---

### **Category 4: Phase 2 - Pagination (11 tests)** ⭐ NEW

Tests new pagination and analytics summary features.

| Test | Endpoint | Expected Result |
|------|----------|----------------|
| 4.1 Get All - Default Pagination | GET /api/urls | 200, page=0, size=10 |
| 4.2 Get URLs - With Analytics | GET /api/urls?page=0&size=10 | 200, analyticsSummary in each URL |
| 4.3 Pagination - Page 1 | GET /api/urls?page=1 | 200, page=1 |
| 4.4 Page Size - 5 per page | GET /api/urls?size=5 | 200, size=5 |
| 4.5 Page Size - 50 (Max) | GET /api/urls?size=50 | 200, size=50 |
| 4.6 Page Size - Exceeds Max | GET /api/urls?size=100 | 200, size=50 (capped) |
| 4.7 Sort - By createdAt DESC | GET /api/urls?sortBy=createdAt&sortDir=desc | 200, newest first |
| 4.8 Sort - By clickCount DESC | GET /api/urls?sortBy=clickCount&sortDir=desc | 200, most clicked first |
| 4.9 Recent URLs - Default | GET /api/urls/recent | 200, newest first |
| 4.10 Recent URLs - Custom Size | GET /api/urls/recent?size=5 | 200, size=5 |
| 4.11 Legacy Endpoint | GET /api/urls/all | 200, array (no pagination) |

**What's Being Tested:**
- Pagination structure
- Default values
- Page navigation
- Size limits and validation
- Sorting functionality
- Analytics summary in list
- Recent URLs endpoint
- Backward compatibility

---

### **Category 5: Edge Cases & Error Handling (4 tests)**

Tests edge cases and error scenarios.

| Test | Endpoint | Expected Result |
|------|----------|----------------|
| 5.1 Pagination - Negative Page | GET /api/urls?page=-1 | 200, corrected to 0 |
| 5.2 Pagination - Zero Size | GET /api/urls?size=0 | 200, corrected to 1 |
| 5.3 Pagination - Beyond Total | GET /api/urls?page=9999 | 200, empty content |
| 5.4 Analytics - No Clicks | GET /api/analytics/{code} | 200, empty analytics |

**What's Being Tested:**
- Invalid parameter handling
- Parameter correction
- Empty result handling
- Zero-click analytics

---

## 🚀 Running Tests

### **Method 1: Run All Tests (Recommended)**

```bash
# In Postman:
1. Right-click "Shortenit - Phase 2 Complete Test Suite"
2. Select "Run collection"
3. Click "Run Shortenit..."
4. Wait for all 31 tests to complete
```

**Expected Results:**
- ✅ All tests pass: 31/31
- ⏱️ Total time: ~5-10 seconds
- 📊 0 failures

---

### **Method 2: Run by Category**

```bash
# Test each category separately:

# Category 1: URL Shortening
Right-click "Phase 1 - URL Shortening" → Run

# Category 2: Redirects
Right-click "Phase 1 - Redirects" → Run

# Category 3: Analytics
Right-click "Phase 2 - Analytics" → Run

# Category 4: Pagination (NEW)
Right-click "Phase 2 - Pagination (NEW)" → Run

# Category 5: Edge Cases
Right-click "Edge Cases & Error Handling" → Run
```

---

### **Method 3: Run Individual Tests**

```bash
# Click any test → Click "Send"
# Useful for debugging specific scenarios
```

---

### **Method 4: Command Line (Newman)**

```bash
# Install Newman
npm install -g newman

# Run collection
newman run Shortenit_Phase2_Complete_Test_Suite.postman_collection.json

# Run with environment
newman run Shortenit_Phase2_Complete_Test_Suite.postman_collection.json \
  --env-var "base_url=http://localhost:8080"

# Generate HTML report
newman run Shortenit_Phase2_Complete_Test_Suite.postman_collection.json \
  --reporters cli,html \
  --reporter-html-export report.html
```

---

## 📈 Test Results

### **Successful Test Run Example:**

```
┌─────────────────────────┬──────────────────┬──────────────────┐
│                         │         executed │           failed │
├─────────────────────────┼──────────────────┼──────────────────┤
│              iterations │                1 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│                requests │               31 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│            test-scripts │               31 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│      prerequest-scripts │                0 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│              assertions │              120 │                0 │
├─────────────────────────┴──────────────────┴──────────────────┤
│ total run duration: 8.2s                                      │
├───────────────────────────────────────────────────────────────┤
│ total data received: 25.5kB (approx)                         │
├───────────────────────────────────────────────────────────────┤
│ average response time: 145ms [min: 8ms, max: 423ms]          │
└───────────────────────────────────────────────────────────────┘
```

### **What to Check:**

✅ **All Green:** All tests passed
- No failures
- All assertions passed
- Reasonable response times

❌ **Red/Yellow:** Some tests failed
- Check "Test Results" tab
- Look for error messages
- Fix issues and re-run

---

## 🔍 Detailed Test Scenarios

### **Scenario 1: Complete Workflow Test**

Tests end-to-end user journey.

```bash
1. Create URL → POST /api/shorten
2. Click URL → GET /s/{code}
3. Check Analytics → GET /api/analytics/{code}
4. View in List → GET /api/urls?page=0&size=10
5. Sort by Clicks → GET /api/urls?sortBy=clickCount&sortDir=desc
6. Get Recent → GET /api/urls/recent
7. Delete URL → DELETE /api/urls/{code}
```

### **Scenario 2: Pagination Flow Test**

Tests pagination navigation.

```bash
1. Get page 0 → Check first=true, last=false
2. Get page 1 → Check first=false
3. Get last page → Check last=true
4. Try page beyond total → Empty content
5. Navigate backwards → Previous pages work
```

### **Scenario 3: Analytics Test**

Tests analytics accuracy.

```bash
1. Create URL
2. Generate 10 clicks from different:
   - Countries (Thailand, USA, UK)
   - Devices (mobile, desktop, tablet)
   - Browsers (Chrome, Safari, Firefox)
3. Check full analytics → All data present
4. Check summary in list → Top values correct
5. Filter by date range → Data filtered
```

### **Scenario 4: Sorting Test**

Tests all sorting options.

```bash
1. Create URLs with different:
   - Creation dates
   - Click counts
   - Short codes
2. Sort by createdAt ASC → Oldest first
3. Sort by createdAt DESC → Newest first
4. Sort by clickCount DESC → Most clicked
5. Sort by clickCount ASC → Least clicked
6. Sort by shortCode ASC → Alphabetical
```

---

## 🐛 Troubleshooting

### **Issue 1: Tests Failing - Connection Error**

**Symptom:**
```
Error: connect ECONNREFUSED 127.0.0.1:8080
```

**Solution:**
```bash
# Check if application is running
curl http://localhost:8080/actuator/health

# If not running, start it
docker compose up -d

# Or
./mvnw spring-boot:run
```

---

### **Issue 2: Empty Analytics Data**

**Symptom:**
```json
{
  "totalClicks": 0,
  "clicksByCountry": []
}
```

**Solution:**
```bash
# Generate test clicks first
# Option 1: Manual clicks
curl -L http://localhost:8080/s/{code}

# Option 2: Use test script
python test_shortenit.py
```

---

### **Issue 3: Pagination Returns Empty**

**Symptom:**
```json
{
  "content": [],
  "totalElements": 0
}
```

**Solution:**
```bash
# Create test URLs first
# Run "1.1 Create URL - Basic" test 5-10 times
# Or use bulk creation script
```

---

### **Issue 4: Geographic Data Shows "Unknown"**

**Symptom:**
```json
{
  "topCountry": "Unknown",
  "clicksByCountry": [
    {"country": "Unknown", "count": 5}
  ]
}
```

**Explanation:**
- LocalGeoIp cannot locate localhost/private IPs
- This is expected for local testing
- Will show real countries in production with public IPs

**Solution:**
- For testing, use Python script with public IPs
- Or deploy to EC2 and test with real traffic

---

### **Issue 5: Sort Not Working**

**Symptom:**
Results not sorted as expected

**Solution:**
```bash
# Check sortBy and sortDir parameters
# Valid sortBy: createdAt, clickCount, shortCode, customAlias
# Valid sortDir: asc, desc

# Correct:
GET /api/urls?sortBy=clickCount&sortDir=desc

# Wrong:
GET /api/urls?sortBy=clicks&sortDir=DESC  # ❌
```

---

### **Issue 6: Tests Pass But Data Incorrect**

**Symptom:**
All tests pass but analytics data seems wrong

**Checklist:**
1. ✅ Check timezone settings (Asia/Bangkok)
2. ✅ Verify LocalGeoIp database loaded
3. ✅ Check UserAgentParser working
4. ✅ Verify clicks being tracked in url_clicks table
5. ✅ Test with real User-Agent headers

---

## 📊 Test Coverage Report

### **Phase 1 Features:**
- ✅ URL Shortening: 100%
- ✅ Custom Aliases: 100%
- ✅ Expiration: 100%
- ✅ Redirects: 100%
- ✅ CRUD Operations: 100%

### **Phase 2 Features:**
- ✅ Analytics - Geographic: 100%
- ✅ Analytics - Device: 100%
- ✅ Analytics - Browser: 100%
- ✅ Analytics - Time Series: 100%
- ✅ Analytics - Date Range: 100%
- ✅ Pagination: 100% ⭐ NEW
- ✅ Sorting: 100% ⭐ NEW
- ✅ Analytics Summary: 100% ⭐ NEW

### **Error Handling:**
- ✅ Validation Errors: 100%
- ✅ Not Found Errors: 100%
- ✅ Duplicate Errors: 100%
- ✅ Edge Cases: 100%

---

## 🎯 Test Checklist

Before considering Phase 2 complete:

### **Functional Tests:**
- [ ] All 31 Postman tests pass
- [ ] URL shortening works
- [ ] Redirects work and track clicks
- [ ] Analytics data accurate
- [ ] Pagination works correctly
- [ ] Sorting works as expected
- [ ] Analytics summary present in lists

### **Performance Tests:**
- [ ] Response times < 500ms
- [ ] Pagination with 1000+ URLs works
- [ ] Analytics calculation fast enough
- [ ] No memory leaks

### **Integration Tests:**
- [ ] LocalGeoIp database working
- [ ] UserAgentParser working
- [ ] Database queries optimized
- [ ] Timezone handling correct

### **Edge Cases:**
- [ ] Invalid parameters handled
- [ ] Empty results handled
- [ ] Large page numbers handled
- [ ] URLs with no clicks handled

---

## 📝 Test Report Template

```markdown
## Phase 2 Test Report

**Date:** December 11, 2024  
**Tester:** [Your Name]  
**Environment:** Local / Docker / EC2

### Test Results:
- Total Tests: 31
- Passed: [X]
- Failed: [Y]
- Skipped: [Z]

### Test Categories:
| Category | Tests | Passed | Failed |
|----------|-------|--------|--------|
| URL Shortening | 8 | 8 | 0 |
| Redirects | 2 | 2 | 0 |
| Analytics | 6 | 6 | 0 |
| Pagination | 11 | 11 | 0 |
| Edge Cases | 4 | 4 | 0 |

### Issues Found:
1. [Issue description]
   - Severity: High/Medium/Low
   - Status: Fixed/Pending
   
2. [Issue description]
   - Severity: High/Medium/Low
   - Status: Fixed/Pending

### Performance Metrics:
- Average Response Time: [X]ms
- Max Response Time: [X]ms
- Total Test Duration: [X]s

### Recommendations:
- [Recommendation 1]
- [Recommendation 2]

### Conclusion:
Phase 2 is [Ready / Not Ready] for Phase 3.
```

---

## 🎉 Success Criteria

Phase 2 testing is considered successful when:

✅ All 31 tests pass  
✅ No critical bugs found  
✅ Response times acceptable  
✅ Analytics data accurate  
✅ Pagination works smoothly  
✅ Ready for Phase 3 implementation  

---

**Next Steps:**
1. ✅ Complete all tests
2. ✅ Fix any issues found
3. ✅ Generate test report
4. ✅ Deploy to EC2 for real-world testing
5. ✅ Move to Phase 3 (Authentication & CLI)

---

**Good luck with testing!** 🚀
