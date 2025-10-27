$dir = 'app\\src\\main\\res\\drawable'
Get-ChildItem -Path $dir -File | Where-Object { $_.Name -match '^[0-9]' } | ForEach-Object {
    $old = $_.FullName
    $newName = 'img_' + $_.Name
    $newPath = Join-Path $dir $newName
    Rename-Item -Path $old -NewName $newName -Force
    Write-Output ("Renamed: $($_.Name) -> $newName")
}
Write-Output 'Done.'
