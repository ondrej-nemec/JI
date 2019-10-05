<table>
	<tr>
		<td><b>Tables</b></td>
		<td><b>Attributes</b></td>
	</tr>	
	<tr>
		<td>Users</td>
		<td>id, rank</td>
	</tr>
	<tr>
		<td>Role</td>
		<td>id</td>
	</tr>
	<tr>
		<td>User_to_role</td>
		<td>user_id, role_id</td>
	</tr>
	<tr>
		<td>Rules_for_users</td>
		<td>destination, action, user_id</td>
	</tr>
	<tr>
		<td>Rules_for_rank</td>
		<td>destination, action, rank</td>
	</tr>
	<tr>
		<td>Rules_for_role</td>
		<td>destination, action, role_id</td>
	</tr>
</table>

```sql
SELECT rules_user.action user_action, rules_rank.action rank_action, rules_role.action role_action
FROM Users users
LEFT JOIN Rules_for_users rules_users ON users.id = rules_users.user_id
LEFT JOIN Rules_for_rank rules_rank ON users.rank >= rules_rank.rank
LEFT JOIN User_to_role user_role ON user.id = user_role.user_id
LEFT JOIN Role role ON role.id = user_role.role_id
LEFT JOIN Rules_for_role rules_role ON role.id = rules_role.role_id
WHERE user.id = ? AND (rules_role.destination = :destination OR rules_rank.destination = :destination OR rules_user.destination = :destination)
```

