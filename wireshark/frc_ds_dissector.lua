frc_ds = Proto ("frc_ds","FRC Driver Station Protocol")

local MODE_VALS = {[0x0] = "Teleoperated", [0x8] = "Autonomous", [0x1] = "Test"}
local ALLIANCE_COLOR_VALS = {[66] = "Blue", [82] = "Red"}
local ALLIANCE_POS_VALS = {[49] = "1", [50] = "2", [51] = "3"}
local fields = {
	pkt_num = ProtoField.uint16("frc_ds.pkt_num", "Packet Number", base.DEC),
	flags_f = ProtoField.uint8("frc_ds.flags", "Status Flags", base.HEX),
	flags = {
		reset = ProtoField.bool("frc_ds.flags.reset", "Reset", 8, nil, 0x80),
		not_estopped = ProtoField.bool("frc_ds.flags.not_estopped", "Not Emergency Stopped", 8, nil, 0x40),
		enabled = ProtoField.bool("frc_ds.flags.enabled", "Enabled", 8, nil, 0x20),
		fms_connected = ProtoField.bool("frc_ds.flags.fms_connected", "FMS Connected", 8, nil, 0x8),
		resync = ProtoField.bool("frc_ds.flags.resync", "Resync", 8, nil, 0x4),
		mode = ProtoField.uint8("frc_ds.flags.mode", "Mode", base.HEX, MODE_VALS, 0x12),
		check_versions_field = ProtoField.bool("frc_ds.flags.check_versions", "Check Versions", 8, nil, 0x1)
	},
	digital_input_f = ProtoField.uint8("frc_ds.digital_input", "Digital Inputs", base.HEX),
	digital_input = {},
	team_num = ProtoField.uint16("frc_ds.team_num", "Team Number", base.DEC),
	alliance_color = ProtoField.uint8("frc_ds.alliance_color", "Alliance Color", base.HEX, ALLIANCE_COLOR_VALS),
	alliance_pos = ProtoField.uint8("frc_ds.alliance_pos", "Alliance Position", base.HEX, ALLIANCE_POS_VALS),
	joystick = {},
	analog_input = {},
	crio_chk = ProtoField.uint64("frc_ds.crio_chk", "cRio Checksum", base.HEX),
	fpga_chk_f = ProtoField.bytes("frc_ds.fpga_chk", "FPGA Checksum", base.HEX),
	fpga_chk = {},
	version = ProtoField.string("frc_ds.version", "Driver Station Version"),
	crc = ProtoField.uint32("frc_ds.crc", "CRC Checksum", base.HEX)
}

do
	local mask = 0x1
	for i = 1, 8 do
		table.insert(fields.digital_input, ProtoField.bool("frc_ds.digital_input."..i, "DI "..i, 8, nil, mask))
		mask = mask * 2
	end
end

for i = 1, 4 do
	table.insert(fields.joystick, {
		field = ProtoField.bytes("frc_ds.joystick."..i, "Joystick "..i),
		axis = {
			ProtoField.int8("frc_ds.joystick."..i..".axis.1", "Axis 1 (X)", base.DEC),
			ProtoField.int8("frc_ds.joystick."..i..".axis.2", "Axis 2 (Y)", base.DEC),
			ProtoField.int8("frc_ds.joystick."..i..".axis.3", "Axis 3 (Z)", base.DEC),
			ProtoField.int8("frc_ds.joystick."..i..".axis.4", "Axis 4 (Twist)", base.DEC),
			ProtoField.int8("frc_ds.joystick."..i..".axis.5", "Axis 5 (Throttle)", base.DEC),
			ProtoField.int8("frc_ds.joystick."..i..".axis.6", "Axis 6", base.DEC)
		},
		button_f = ProtoField.uint8("frc_ds.joystick."..i..".button", "Buttons", base.HEX),
		button = {
			ProtoField.bool("frc_ds.joystick."..i..".button.1", "Button 1 (Trigger)", 16, nil, 0x1),
			ProtoField.bool("frc_ds.joystick."..i..".button.2", "Button 2 (Top)", 16, nil, 0x2)
		}
	})
	local mask = 0x4
	for b = 3, 12 do
		table.insert(fields.joystick[i].button, ProtoField.bool("frc_ds.joystick_"..i..".button."..b, "Button "..b, 16, nil, mask))
		mask = mask * 2
	end
end

for i = 1, 4 do
	table.insert(fields.analog_input, ProtoField.uint16("frc_ds.analog_input."..i, "Analog Input "..i, base.DEC))
end

for i = 1, 4 do
	table.insert(fields.fpga_chk, ProtoField.uint32("frc_ds.fpga_chk."..i, "Checksum "..i, base.HEX))
end

local function flatten(list)
	if type(list) ~= "table" then return {list} end
	local flat_list = {}
	for _, elem in pairs(list) do
		for _, val in pairs(flatten(elem)) do
			flat_list[#flat_list + 1] = val
		end
	end
	return flat_list
end

frc_ds.fields = flatten(fields)

-- frc_ds dissector function
function frc_ds.dissector (buf, pkt, root)
	-- make sure the packet is the right length
	if buf:len() ~= 1024 then return end
	pkt.cols.protocol = frc_ds.name

	-- create subtree
	subtree = root:add(frc_ds, buf(0))

  	subtree:add(fields.pkt_num, buf(0, 2))
	local flags = buf(2, 1)
	flags_subtree = subtree:add(fields.flags_f, flags)
	for i, flag in pairs(fields.flags) do
		flags_subtree:add(flag, flags)
	end
	local digital_input = buf(3, 1)
	local digital_input_subtree = subtree:add(fields.digital_input_f, digital_input)
	for i, di in ipairs(fields.digital_input) do
		digital_input_subtree:add(di, digital_input)
	end
	subtree:add(fields.team_num, buf(4, 2))
	subtree:add(fields.alliance_color, buf(6, 1))
	subtree:add(fields.alliance_pos, buf(7, 1))
	for i, j in ipairs(fields.joystick) do
		local offset = 8 * i
		local joystick_subtree = subtree:add(j.field, buf(offset, 8))
		for n, a in ipairs(j.axis) do
			joystick_subtree:add(a, buf((offset - 1) + n, 1))
		end
		local joystick_button = buf(offset + 6, 2)
		local joystick_button_subtree = joystick_subtree:add(j.button_f, joystick_button)
		for n, b in ipairs(j.button) do
			joystick_button_subtree:add(b, joystick_button)
		end
	end
	for i, j in ipairs(fields.analog_input) do
		subtree:add(j, buf(38 + 2 * i, 2))
	end
	subtree:add(fields.crio_chk, buf(48, 8))
	fpga_chk_subtree = subtree:add(fields.fpga_chk_f, buf(56, 16))
	for i, c in ipairs(fields.fpga_chk) do
		fpga_chk_subtree:add(c, buf(52 + 4 * i, 4))
	end
	subtree:add(fields.version, buf(72, 8))
	subtree:add(fields.crc, buf(1020, 4))
end
 
-- Initialization routine
function frc_ds.init()
end
 
-- register a chained dissector for port 1110
DissectorTable.get("udp.port"):add(1110, frc_ds)
