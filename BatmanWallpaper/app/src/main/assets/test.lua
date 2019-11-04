--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/10/26
-- Time: 20:28
-- To change this template use File | Settings | File Templates.
--

function getUrl()
    return "http://cnzhuitao.cn/index.html"
end

function needWorking(str)
    --match = "<li><span class=\"date\">10--06</span><a href=\"http://cnzhuitao.cn/Ajitongjiling/1088.html\" target=\"_blank\"><strong>A级通缉令：乐先木</strong></a></li>"
    local matchA = "<li><span class=\"date\">(%d*--%d*)</span><a href=\"[^\"]+\" target=\"_blank\"><strong>A级通缉令[\xEF:][\xBC]?[\x9A]?([^<]+)</strong></a></li>"
    local matchB = "<li><span class=\"date\">(%d*--%d*)</span><a href=\"[^\"]+\" target=\"_blank\"><strong>B级通缉令[\xEF:][\xBC]?[\x9A]?([^<]+)</strong></a></li>"
    --local matchB = "<li><span class=\"date\">(%d*--%d*)</span><a href=\"http://cnzhuitao.cn/Ajitongjiling/1088.html\" target=\"_blank\"><strong>B级通缉令：([^<]+)</strong></a></li>"
    --match = "10--06"
    --match = "0-06</span><a href=\"http://cnzhuitao.cn/Ajitongjiling/1088.html\" target=\"_blank\"><strong>A级通缉令：乐先木</strong></a></li>"
    --print(matchA)
    local timeA, nameA = string.match(str, matchA, 1);
    local timeB, nameB= string.match(str, matchB, 1);

    local getTime = os.date("%m-%d")
    --print(getTime)

    if(timeA == nil and timeB == nil)
    then
        return "error", "", "", ""
    end


    if(timeA ~= nil and timeA >= getTime)
    then
        return "true", "A", nameA, timeA
    end

    if(timeB ~= nil and timeB >= getTime)
    then
        return "true", "B", nameB, timeB
    end

    if(timeA ~= nil )
    then
        return "false", "A", nameA, timeA
    else
        if(timeB ~= nil)
        then
            return "false", "B", nameB, timeB
        end
    end
end

